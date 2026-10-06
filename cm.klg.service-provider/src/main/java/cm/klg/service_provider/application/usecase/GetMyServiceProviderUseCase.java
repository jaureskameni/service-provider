package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView2;
import cm.klg.service_provider.domain.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMyServiceProviderUseCase {
  private final ServiceProviderRepository repository;

  public ServiceProviderView2 execute(UserId userId) {
    return repository.loadAsView2ByUserId(userId);
  }
}
