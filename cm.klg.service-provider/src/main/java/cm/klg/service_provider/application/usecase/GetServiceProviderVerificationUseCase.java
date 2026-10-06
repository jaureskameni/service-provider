package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderVerificationView;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServiceProviderVerificationUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public ServiceProviderVerificationView execute(ServiceProviderId serviceProviderId) {
    return serviceProviderRepository.loadForVerification(serviceProviderId);
  }
}
