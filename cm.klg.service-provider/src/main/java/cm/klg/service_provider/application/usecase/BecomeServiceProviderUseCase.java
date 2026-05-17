package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderWithPhoneNumberAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.ArrayList;

public record BecomeServiceProviderUseCase(ServiceProviderRepository serviceProviderRepository) {
  public ServiceProviderId execute(BecomeServiceProviderCommand command) {
    if (serviceProviderRepository.existsByUserId(command.userId())) {
      throw new ServiceProviderAlreadyExistsException();
    }
    if (serviceProviderRepository.existsByPhoneNumber(command.phoneNumber())) {
      throw new ServiceProviderWithPhoneNumberAlreadyExistsException();
    }

    ServiceProvider serviceProvider =
        ServiceProvider.of(
            command.userId, command.city, command.district, command.phoneNumber, new ArrayList<>());

    serviceProvider.addUserService(
        command.serviceTypeId, command.yearOfExperience, command.document);

    serviceProviderRepository.insert(serviceProvider);

    return serviceProvider.getId();
  }

  public record BecomeServiceProviderCommand(
      UserId userId,
      UserCityId city,
      UserDistrictId district,
      PhoneNumber phoneNumber,
      ServiceTypeId serviceTypeId,
      YearOfExperience yearOfExperience,
      UserDocument document) {}
}
