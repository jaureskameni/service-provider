package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdatePortfolioItemUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final DomainEventPublisher domainEventPublisher;

  public void execute(Command command) {
    var serviceProvider = serviceProviderRepository.loadByUserId(command.userId());
    serviceProvider.updatePortfolioItem(
        command.portfolioItemId(), command.title(), command.description(), command.mediaId());
    serviceProviderRepository.update(serviceProvider);
    domainEventPublisher.serviceProviderPortfolioItemUpdatedEvent(
        serviceProvider.toPortfolioItemUpdatedEvent(command.portfolioItemId()));
  }

  public record Command(
      UserId userId,
      PortfolioItemId portfolioItemId,
      PortfolioItemTitle title,
      PortfolioItemDescription description,
      PortfolioItemMediaId mediaId) {}
}
