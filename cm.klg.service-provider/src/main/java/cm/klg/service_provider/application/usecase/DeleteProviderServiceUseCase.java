package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteProviderServiceUseCase {
  private final ServiceProviderRepository repository;

  public void execute(UserId userId, ServiceTypeId serviceTypeId) {
    var provider = repository.loadByUserId(userId);
    provider.deleteUserService(serviceTypeId);
    repository.update(provider);
  }
}
