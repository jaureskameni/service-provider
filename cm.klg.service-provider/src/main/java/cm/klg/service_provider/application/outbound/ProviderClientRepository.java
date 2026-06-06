package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.provider_client.ProviderClient;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;

public interface ProviderClientRepository {
  boolean existsByUserIdAndProviderId(UserId userId, ServiceProviderId providerId);

  void insert(ProviderClient providerClient);
}
