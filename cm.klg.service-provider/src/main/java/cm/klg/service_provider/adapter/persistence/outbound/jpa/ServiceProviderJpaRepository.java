package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
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
    ServiceProviderSpringRepository serviceProviderSpringRepository, JpaMapper jpaMapper)
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
  public ServiceProvider load(@NonNull ServiceProviderId serviceProviderId) {
    return serviceProviderSpringRepository
        .findAggregateById(serviceProviderId.value())
        .map(jpaMapper::toServiceProviderDomain)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  @Override
  public ServiceProvider loadByUserId(@NonNull UserId userId) {
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
  public PageData<ServiceProviderView1> loadAllAsView1(@NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    return toPageData(serviceProviderSpringRepository.findAllIds(pageable));
  }

  @Override
  public PageData<ServiceProviderView1> loadAllByStatusAsView1(
      @NonNull ServiceProviderStatus serviceProviderStatus,
      @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    return toPageData(
        serviceProviderSpringRepository.findAllIdsByStatus(serviceProviderStatus.name(), pageable));
  }

  @Override
  public PageData<ServiceProviderView1> searchByLocationAndStatus(
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
  public ServiceProviderView1 loadAsView1(@NonNull ServiceProviderId serviceProviderId) {
    return serviceProviderSpringRepository
        .findAggregateById(serviceProviderId.value())
        .map(jpaMapper::toServiceProviderView1)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  private PageData<ServiceProviderView1> toPageData(Page<UUID> serviceProviderIds) {
    if (serviceProviderIds.isEmpty()) {
      return new PageData<>(serviceProviderIds.getTotalElements(), Collections.emptyList());
    }
    List<UUID> ids = serviceProviderIds.getContent();
    Map<UUID, ServiceProviderJpa> serviceProvidersById =
        serviceProviderSpringRepository.findAllAggregatesByIdIn(ids).stream()
            .collect(Collectors.toMap(ServiceProviderJpa::getId, Function.identity()));
    return new PageData<>(
        serviceProviderIds.getTotalElements(),
        ids.stream()
            .map(serviceProvidersById::get)
            .map(jpaMapper::toServiceProviderView1)
            .toList());
  }
}
