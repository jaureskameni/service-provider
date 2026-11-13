package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMyServiceProviderApplicationStatusUseCase {
  private final ServiceProviderRepository repository;

  public Optional<ServiceProviderStatus> execute(UserId userId) {
    return repository.findStatusByUserId(userId);
  }
}
