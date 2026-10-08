package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView3;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetProfileImageReviewUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public ServiceProviderView3 execute(ServiceProviderId id) {
    return serviceProviderRepository.loadPendingProfileImageReviewAsView3(id);
  }
}
