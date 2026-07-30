package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetProviderServicesUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public List<UserServiceView> execute(ServiceProviderId serviceProviderId) {
    return serviceProviderRepository.loadAllProviderServices(serviceProviderId);
  }
}
