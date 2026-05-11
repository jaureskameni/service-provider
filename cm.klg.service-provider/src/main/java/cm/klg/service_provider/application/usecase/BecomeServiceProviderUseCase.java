package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProvider;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProviderAlreadyExistsException;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProviderId;
import cm.klg.service_provider.domain.ServiceProvider.UserCityId;
import cm.klg.service_provider.domain.ServiceProvider.UserDistrictId;
import cm.klg.service_provider.domain.ServiceProvider.UserDocument;
import cm.klg.service_provider.domain.ServiceProvider.YearOfExperience;
import cm.klg.service_provider.domain.ServiceType.ServiceTypeId;
import cm.klg.service_provider.domain.UserId;
import java.util.ArrayList;

public record BecomeServiceProviderUseCase(ServiceProviderRepository serviceProviderRepository) {
  public ServiceProviderId execute(BecomeServiceProviderCommand command) {
    if (serviceProviderRepository.existsByUserId(command.userId())) {
      throw new ServiceProviderAlreadyExistsException();
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
