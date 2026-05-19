package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServiceProviderByIdUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public ServiceProviderView1 execute(ServiceProviderId serviceProviderId) {
    return serviceProviderRepository.loadAsView1(serviceProviderId);
  }
}
