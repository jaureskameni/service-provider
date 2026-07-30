package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.UserId;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllMyServicesUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public List<UserServiceView> execute(UserId userId) {
    return serviceProviderRepository.loadAllMyServices(userId);
  }
}
