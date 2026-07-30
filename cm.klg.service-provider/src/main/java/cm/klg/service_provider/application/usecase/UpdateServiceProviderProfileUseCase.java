package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateServiceProviderProfileUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(Command command) {
    var serviceProvider = serviceProviderRepository.loadByUserId(command.userId());
    serviceProvider.updateProfile(command.location(), command.phoneNumber(), command.about());
    serviceProviderRepository.update(serviceProvider);
  }

  public record Command(
      UserId userId, ProviderLocation location, PhoneNumber phoneNumber, AboutProvider about) {}
}
