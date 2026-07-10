package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record ServiceProviderJpaRepository(
    ServiceProviderSpringRepository serviceProviderSpringRepository,
    UserSpringRepository userSpringRepository,
    ServiceTypeSpringRepository serviceTypeSpringRepository,
    JpaMapper jpaMapper)
    implements ServiceProviderRepository {
  @Override
  public void insert(@NonNull ServiceProvider serviceProvider) {
    serviceProviderSpringRepository.save(jpaMapper.toServiceProviderJpa(serviceProvider));
  }

  @Override
  public boolean existsByUserId(@NonNull UserId userId) {
    return serviceProviderSpringRepository.existsByUserId(userId.value());
  }

  @Override
  public boolean existsByPhoneNumber(@NonNull PhoneNumber phoneNumber) {
    return serviceProviderSpringRepository.existsByPhoneNumber(
        new PhoneNumberJpa(phoneNumber.countryCode(), phoneNumber.number()));
  }

  @Override
  public ServiceProvider load(@NonNull ServiceProviderId serviceProviderId)
      throws ServiceProviderNotFoundException {
    return serviceProviderSpringRepository
        .findAggregateById(serviceProviderId.value())
        .map(jpaMapper::toServiceProviderDomain)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  @Override
  public ServiceProvider loadByUserId(@NonNull UserId userId)
      throws ServiceProviderNotFoundException {
    return serviceProviderSpringRepository
        .findAggregateByUserId(userId.value())
        .map(jpaMapper::toServiceProviderDomain)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  @Override
  public void update(@NonNull ServiceProvider serviceProvider) {
    serviceProviderSpringRepository
        .findAggregateById(serviceProvider.getId().value())
        .ifPresent(
            serviceProviderJpa -> {
              jpaMapper.toServiceProviderJpa(serviceProviderJpa, serviceProvider);
              serviceProviderSpringRepository.save(serviceProviderJpa);
            });
  }

  @Override
  public PageData<ServiceProviderView> loadAllAsView(@NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    return toPageData(serviceProviderSpringRepository.findAllIds(pageable));
  }

  @Override
  public PageData<ServiceProviderView> loadAllByStatusAsView(
      @NonNull ServiceProviderStatus serviceProviderStatus,
      @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    return toPageData(
        serviceProviderSpringRepository.findAllIdsByStatus(serviceProviderStatus.name(), pageable));
  }

  @Override
  public PageData<ServiceProviderView> searchByLocationAndStatus(
      @NonNull ServiceTypeId serviceTypeId,
      @NonNull UserCityId cityId,
      @Nullable UserDistrictId districtId,
      @Nullable UserQuarterId quarterId,
      @NonNull ServiceProviderStatus status,
      @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    return toPageData(
        serviceProviderSpringRepository.searchIdsByLocationAndStatus(
            serviceTypeId.value(),
            cityId.value(),
            districtId != null ? districtId.value() : null,
            quarterId != null ? quarterId.value() : null,
            status.name(),
            pageable));
  }

  @Override
  public ServiceProviderView loadAsView(@NonNull ServiceProviderId serviceProviderId) {
    return serviceProviderSpringRepository
        .findAggregateById(serviceProviderId.value())
        .map(this::toView)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  @Override
  public ServiceProviderView loadProfile(@NonNull ServiceProviderId serviceProviderId)
      throws ServiceProviderNotFoundException {
    return serviceProviderSpringRepository
        .findAggregateByIdAndStatus(
            serviceProviderId.value(), ServiceProviderStatus.APPROVED.name())
        .map(this::toView)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  private PageData<ServiceProviderView> toPageData(Page<UUID> serviceProviderIds) {
    if (serviceProviderIds.isEmpty()) {
      return new PageData<>(serviceProviderIds.getTotalElements(), Collections.emptyList());
    }

    List<UUID> ids = serviceProviderIds.getContent();

    Map<UUID, ServiceProviderJpa> serviceProvidersById =
        serviceProviderSpringRepository.findAllAggregatesByIdIn(ids).stream()
            .collect(Collectors.toMap(ServiceProviderJpa::getId, Function.identity()));

    Map<UUID, UserJpa> usersById =
        userSpringRepository
            .findAllByIdentityIdIn(
                serviceProvidersById.values().stream().map(ServiceProviderJpa::getUserId).toList())
            .stream()
            .collect(Collectors.toMap(UserJpa::getId, userJpa -> userJpa));

    List<ServiceTypeJpa> serviceTypes =
        serviceTypeSpringRepository.findAllById(
            serviceProvidersById.values().stream()
                .flatMap(sp -> sp.getUserServices().stream())
                .map(us -> us.getId().getServiceTypeId())
                .distinct()
                .toList());

    return new PageData<>(
        serviceProviderIds.getTotalElements(),
        ids.stream()
            .map(serviceProvidersById::get)
            .map(
                sp ->
                    jpaMapper.toServiceProviderView(
                        sp, usersById.get(sp.getUserId()), serviceTypes))
            .toList());
  }

  private ServiceProviderView toView(ServiceProviderJpa serviceProviderJpa) {
    UserJpa userJpa =
        userSpringRepository.findByIdentityId(serviceProviderJpa.getUserId()).orElseThrow();
    List<ServiceTypeJpa> serviceTypeJpas =
        serviceTypeSpringRepository.findAllById(
            serviceProviderJpa.getUserServices().stream()
                .map(us -> us.getId().getServiceTypeId())
                .toList());
    return jpaMapper.toServiceProviderView(serviceProviderJpa, userJpa, serviceTypeJpas);
  }
}
