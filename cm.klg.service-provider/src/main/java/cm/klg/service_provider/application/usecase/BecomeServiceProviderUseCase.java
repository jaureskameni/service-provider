package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.IdentityDocuments;
import cm.klg.service_provider.domain.service_provider.ProfileImageMediaId;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderWithPhoneNumberAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.service_type.ServiceTypeNotFoundException;
import cm.klg.service_provider.domain.user.UserNotFoundException;
import cm.klg.service_provider.domain.user.UserPhoneNumberAlreadyExistsException;
import java.util.ArrayList;

public record BecomeServiceProviderUseCase(
    ServiceProviderRepository serviceProviderRepository,
    UserRepository userRepository,
    ServiceTypeRepository serviceTypeRepository) {
  public ServiceProviderId execute(BecomeServiceProviderCommand command) {
    var profile = command.profile();
    var initialService = command.initialService();
    if (!userRepository.existsByUserId(command.userId())) {
      throw new UserNotFoundException();
    }
    if (!serviceTypeRepository.existsById(initialService.serviceTypeId())) {
      throw new ServiceTypeNotFoundException();
    }
    if (serviceProviderRepository.existsByUserId(command.userId())) {
      throw new ServiceProviderAlreadyExistsException();
    }
    if (serviceProviderRepository.existsByPhoneNumber(profile.phoneNumber())) {
      throw new ServiceProviderWithPhoneNumberAlreadyExistsException();
    }
    if (userRepository.existsByPhoneNumberExceptUserId(profile.phoneNumber(), command.userId())) {
      throw new UserPhoneNumberAlreadyExistsException();
    }

    ServiceProvider serviceProvider =
        ServiceProvider.of(
            command.userId(),
            profile.location(),
            profile.phoneNumber(),
            profile.about(),
            profile.identityDocuments(),
            profile.profileImageId(),
            new ArrayList<>());

    serviceProvider.addUserService(
        initialService.serviceTypeId(),
        initialService.yearOfExperience(),
        initialService.document());

    serviceProviderRepository.insert(serviceProvider);

    return serviceProvider.getId();
  }

  public record BecomeServiceProviderCommand(
      UserId userId, ProviderRegistrationProfile profile, InitialProviderService initialService) {}

  public record ProviderRegistrationProfile(
      ProviderLocation location,
      PhoneNumber phoneNumber,
      AboutProvider about,
      IdentityDocuments identityDocuments,
      ProfileImageMediaId profileImageId) {}

  public record InitialProviderService(
      ServiceTypeId serviceTypeId, YearOfExperience yearOfExperience, UserDocument document) {}
}
