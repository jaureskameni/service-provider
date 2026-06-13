package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RejectServiceProviderRequestUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final DomainEventPublisher domainEventPublisher;

  public void execute(UserId userId, ServiceProviderId serviceProviderId, RejectionReason reason) {
    ServiceProvider serviceProvider = serviceProviderRepository.load(serviceProviderId);

    serviceProvider.reject(userId, reason);

    serviceProviderRepository.update(serviceProvider);

    domainEventPublisher.serviceProviderRejectedEvent(serviceProvider.toRejectedEvent());
  }
}
