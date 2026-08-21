package cm.klg.service_provider.application.usecase;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.provider_client.ProviderClient;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;

public record CreateNewProviderClientUseCase(ProviderClientRepository providerClientRepository) {
  public void execute(Command command) {
    UserId userId = command.userId;
    ServiceProviderId providerId = command.providerId;
    providerClientRepository.insertIfAbsent(ProviderClient.of(userId, providerId));
  }

  public record Command(ServiceProviderId providerId, UserId userId, CreatedAt createdAt) {}
}
