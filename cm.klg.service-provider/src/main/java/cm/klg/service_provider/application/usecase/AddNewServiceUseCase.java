package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddNewServiceUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(AddNewServiceCommand command) {
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
