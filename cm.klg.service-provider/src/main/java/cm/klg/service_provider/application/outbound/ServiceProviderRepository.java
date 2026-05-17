package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProvider;
import cm.klg.service_provider.domain.UserId;

public interface ServiceProviderRepository {
  void insert(ServiceProvider serviceProvider);

  boolean existsByUserId(UserId userId);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);
}
