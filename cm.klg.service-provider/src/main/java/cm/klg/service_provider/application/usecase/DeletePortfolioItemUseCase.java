package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeletePortfolioItemUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(UserId userId, PortfolioItemId portfolioItemId) {
    var serviceProvider = serviceProviderRepository.loadByUserId(userId);
    serviceProvider.deletePortfolioItem(portfolioItemId);
    serviceProviderRepository.update(serviceProvider);
  }
}
