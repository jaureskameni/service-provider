package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProviderWithPhoneNumberAlreadyExistsException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateServiceProviderProfileUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final DomainEventPublisher domainEventPublisher;

  public void execute(Command command) {
    var serviceProvider = serviceProviderRepository.loadByUserId(command.userId());
    if (serviceProviderRepository.existsByPhoneNumberExceptProviderId(
        command.phoneNumber(), serviceProvider.getId())) {
      throw new ServiceProviderWithPhoneNumberAlreadyExistsException();
    }
    serviceProvider.updateProfile(command.location(), command.phoneNumber(), command.about());
    serviceProviderRepository.update(serviceProvider);
    domainEventPublisher.serviceProviderProfileUpdatedEvent(
        serviceProvider.toProfileUpdatedEvent());
  }

  public record Command(
      UserId userId, ProviderLocation location, PhoneNumber phoneNumber, AboutProvider about) {}
}
