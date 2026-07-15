package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.service_type.ServiceTypeNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddNewServiceUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final ServiceTypeRepository serviceTypeRepository;

  public void execute(AddNewServiceCommand command) {
    if (!serviceTypeRepository.existsById(command.serviceTypeId())) {
      throw new ServiceTypeNotFoundException();
    }
    ServiceProvider serviceProvider = serviceProviderRepository.loadByUserId(command.userId());

    serviceProvider.addUserService(
        command.serviceTypeId(), command.yearOfExperience(), command.userDocument());

    serviceProviderRepository.update(serviceProvider);
  }

  public record AddNewServiceCommand(
      UserId userId,
      ServiceTypeId serviceTypeId,
      YearOfExperience yearOfExperience,
      UserDocument userDocument) {}
}
