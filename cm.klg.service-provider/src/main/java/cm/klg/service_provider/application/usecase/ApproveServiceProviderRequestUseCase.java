package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApproveServiceProviderRequestUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final UserRepository userRepository;
  private final DomainEventPublisher domainEventPublisher;

  public void execute(UserId userId, ServiceProviderId serviceProviderId) {
    ServiceProvider serviceProvider = serviceProviderRepository.load(serviceProviderId);
    User providerUser = userRepository.load(serviceProvider.getUserId());

    serviceProvider.approve(userId);

    serviceProviderRepository.update(serviceProvider);

    domainEventPublisher.serviceProviderApprovedEvent(
        serviceProvider.toApprovedEvent(providerUser));
  }
}
