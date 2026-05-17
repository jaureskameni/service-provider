package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProvider;
import cm.klg.service_provider.domain.UserId;
import org.jspecify.annotations.NonNull;

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
}
