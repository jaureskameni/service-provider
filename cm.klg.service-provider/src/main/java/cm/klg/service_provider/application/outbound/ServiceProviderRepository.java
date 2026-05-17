package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;

public interface ServiceProviderRepository {
  void insert(ServiceProvider serviceProvider);

  boolean existsByUserId(UserId userId);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);

  ServiceProvider load(ServiceProviderId serviceProviderId);

  void update(ServiceProvider serviceProvider);
}
