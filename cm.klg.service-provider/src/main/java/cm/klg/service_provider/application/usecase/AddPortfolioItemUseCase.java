package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddPortfolioItemUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final DomainEventPublisher domainEventPublisher;

  public void execute(Command command) {
    ServiceProvider serviceProvider = serviceProviderRepository.loadByUserId(command.userId());

    var portfolioItem =
        serviceProvider.addPortfolioItem(command.title(), command.description(), command.mediaId());

    serviceProviderRepository.update(serviceProvider);
    domainEventPublisher.serviceProviderPortfolioItemAddedEvent(
        serviceProvider.toPortfolioItemAddedEvent(portfolioItem));
  }

  public record Command(
      UserId userId,
      PortfolioItemTitle title,
      PortfolioItemDescription description,
      PortfolioItemMediaId mediaId) {}
}
