package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.domain.UserId;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllMyPortfolioUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public List<PortfolioView> execute(UserId userId) {
    return serviceProviderRepository.loadAllPortfolio(userId);
  }
}
