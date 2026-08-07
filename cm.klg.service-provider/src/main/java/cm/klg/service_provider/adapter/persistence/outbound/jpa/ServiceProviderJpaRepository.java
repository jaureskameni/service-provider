package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView2;
import cm.klg.service_provider.application.views.UserServiceView;
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
        .ifPresentOrElse(
            serviceProviderJpa -> {
              jpaMapper.fromServiceProvider(serviceProviderJpa, serviceProvider);
              serviceProviderSpringRepository.save(serviceProviderJpa);
            },
            () -> {
              throw new ServiceProviderNotFoundException();
            });
  }

  @Override
  public PageData<ServiceProviderView1> loadAllAsView(@NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    return toPageData(serviceProviderSpringRepository.findAllAsView(pageable));
  }

  @Override
  public PageData<ServiceProviderView1> loadAllByStatusAsView(
      @NonNull ServiceProviderStatus serviceProviderStatus,
      @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    return toPageData(
        serviceProviderSpringRepository.findAllByStatus(serviceProviderStatus.name(), pageable));
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
  public ServiceProviderView2 loadAsView2(@NonNull ServiceProviderId serviceProviderId) {
    ServiceProviderJpa serviceProviderJpa =
        serviceProviderSpringRepository
            .findById(serviceProviderId.value())
            .orElseThrow(ServiceProviderNotFoundException::new);

    UserJpa userJpa =
        userSpringRepository
            .findByIdentityId(serviceProviderJpa.getUserId())
            .orElseThrow(ServiceProviderNotFoundException::new);

    List<UserServiceJpa> userServices =
        serviceProviderSpringRepository.findUserServicesByServiceProviderId(
            serviceProviderId.value());

    List<PortfolioItemJpa> portfolioItems =
        serviceProviderSpringRepository.findPortfolioItemsByServiceProviderId(
            serviceProviderId.value());

    return jpaMapper.toServiceProviderView2(
        serviceProviderJpa, userJpa, userServices, portfolioItems);
  }

  @Override
  public ServiceProviderView1 loadAsView1(UserId userId) throws ServiceProviderNotFoundException {
    return serviceProviderSpringRepository
        .findAggregateByUserId(userId.value(), ServiceProviderStatus.APPROVED.name())
        .map(this::toView1)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  @Override
  public List<PortfolioView> loadAllMyPortfolio(@NonNull UserId userId) {
    return serviceProviderSpringRepository.findPortfolioItemsByUserId(userId.value()).stream()
        .map(jpaMapper::toPortfolioView)
        .toList();
  }

  @Override
  public List<PortfolioView> loadAllProviderPortfolio(@NonNull ServiceProviderId providerId) {
    return serviceProviderSpringRepository
        .findPortfolioItemsByProviderId(providerId.value())
        .stream()
        .map(jpaMapper::toPortfolioView)
        .toList();
  }

  @Override
  public List<UserServiceView> loadAllProviderServices(@NonNull ServiceProviderId providerId) {
    load(providerId);
    return loadUserServiceViews(providerId.value());
  }

  @Override
  public List<UserServiceView> loadAllMyServices(@NonNull UserId userId) {
    return loadUserServiceViews(loadByUserId(userId).getId().value());
  }

  private PageData<ServiceProviderView1> toPageData(Page<ServiceProviderJpa> serviceProviderJpas) {
    if (serviceProviderJpas.isEmpty()) {
      return new PageData<>(serviceProviderJpas.getTotalElements(), Collections.emptyList());
    }

    List<ServiceProviderJpa> providerJpasContent = serviceProviderJpas.getContent();

    List<UUID> userIds =
        serviceProviderJpas.getContent().stream().map(ServiceProviderJpa::getUserId).toList();

    Map<UUID, UserJpa> usersById =
        userSpringRepository.findAllByIdentityIdIn(userIds).stream()
            .collect(Collectors.toMap(UserJpa::getIdentityId, Function.identity()));

    return new PageData<>(
        serviceProviderJpas.getTotalElements(),
        providerJpasContent.stream()
            .map(sp -> jpaMapper.toServiceProviderView1(sp, usersById.get(sp.getUserId())))
            .toList());
  }

  private ServiceProviderView1 toView1(ServiceProviderJpa serviceProviderJpa) {
    UserJpa userJpa =
        userSpringRepository.findByIdentityId(serviceProviderJpa.getUserId()).orElseThrow();
    return jpaMapper.toServiceProviderView1(serviceProviderJpa, userJpa);
  }

  private List<UserServiceView> loadUserServiceViews(UUID serviceProviderId) {
    List<UserServiceJpa> userServices =
        serviceProviderSpringRepository.findUserServicesByServiceProviderId(serviceProviderId);
    List<UUID> serviceTypeIds =
        userServices.stream().map(service -> service.getId().getServiceTypeId()).toList();
    return jpaMapper.toUserServiceViews(
        userServices, serviceTypeSpringRepository.findAllById(serviceTypeIds));
  }
}
