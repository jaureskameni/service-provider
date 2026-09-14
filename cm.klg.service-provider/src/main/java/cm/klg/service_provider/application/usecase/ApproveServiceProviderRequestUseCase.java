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

  public void execute(UserId adminId, ServiceProviderId serviceProviderId) {
    ServiceProvider serviceProvider = serviceProviderRepository.load(serviceProviderId);

    serviceProvider.approve(adminId);

    serviceProviderRepository.update(serviceProvider);

    User providerUser = userRepository.load(serviceProvider.getUserId());

    providerUser.promoteToProvider();
    userRepository.update(providerUser);

    domainEventPublisher.serviceProviderApprovedEvent(serviceProvider.toApprovedEvent());
  }
}
