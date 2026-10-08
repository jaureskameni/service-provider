package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProfileImageMediaId;
import cm.klg.service_provider.domain.service_provider.ProviderProfile;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderWithPhoneNumberAlreadyExistsException;
import cm.klg.service_provider.domain.user.UserPhoneNumberAlreadyExistsException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateApprovedServiceProviderUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final UserRepository userRepository;

  public void execute(Command command) {
    verifyContactUniqueness(command.userId(), command.profile());
    ServiceProvider provider = serviceProviderRepository.loadByUserId(command.userId());
    provider.updateApprovedProfile(
        command.profile().contact().location(),
        command.profile().contact().phoneNumber(),
        command.profile().about(),
        command.profileImageId());
    serviceProviderRepository.update(provider);
  }

  private void verifyContactUniqueness(UserId userId, ProviderProfile profile) {
    var phoneNumber = profile.contact().phoneNumber();
    if (serviceProviderRepository.existsByPhoneNumberExceptUserId(phoneNumber, userId)) {
      throw new ServiceProviderWithPhoneNumberAlreadyExistsException();
    }
    if (userRepository.existsByPhoneNumberExceptUserId(phoneNumber, userId)) {
      throw new UserPhoneNumberAlreadyExistsException();
    }
  }

  public record Command(
      UserId userId, ProviderProfile profile, ProfileImageMediaId profileImageId) {}
}
