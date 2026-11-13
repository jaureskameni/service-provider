package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProfileImageMediaId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateApprovedServiceProviderUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(Command command) {
    var serviceProvider = serviceProviderRepository.loadByUserId(command.userId());
    serviceProvider.updateProfileImage(command.profileImageId());
    serviceProviderRepository.update(serviceProvider);
  }

  public record Command(UserId userId, ProfileImageMediaId profileImageId) {}
}
