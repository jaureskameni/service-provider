package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.utils.PageData;
import cm.klg.service_provider.utils.PaginationFetchRequest;
import org.jspecify.annotations.NonNull;
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
    return toPageData(serviceProviderSpringRepository.findAllAggregate(pageable));
  }

  @Override
  public PageData<ServiceProviderView1> loadAllByStatusAsView1(
      @NonNull ServiceProviderStatus serviceProviderStatus,
      @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    return toPageData(
        serviceProviderSpringRepository.findAllAggregateByStatus(
            serviceProviderStatus.name(), pageable));
  }

  @Override
  public ServiceProviderView1 loadAsView1(@NonNull ServiceProviderId serviceProviderId) {
    return serviceProviderSpringRepository
        .findAggregateById(serviceProviderId.value())
        .map(jpaMapper::toServiceProviderView1)
        .orElseThrow(ServiceProviderNotFoundException::new);
  }

  private PageData<ServiceProviderView1> toPageData(Page<ServiceProviderJpa> serviceProviders) {
    return new PageData<>(
        serviceProviders.getTotalElements(),
        serviceProviders.getContent().stream().map(jpaMapper::toServiceProviderView1).toList());
  }
}
