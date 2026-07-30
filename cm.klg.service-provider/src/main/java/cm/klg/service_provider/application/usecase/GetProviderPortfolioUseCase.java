package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetProviderPortfolioUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public List<PortfolioView> execute(ServiceProviderId providerId) {
    return serviceProviderRepository.loadAllProviderPortfolio(providerId);
  }
}
