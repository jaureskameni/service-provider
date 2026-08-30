package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderCreatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemAddedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemDeletedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemUpdatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileUpdatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderServiceAddedEvent;

public interface DomainEventPublisher {
  void serviceProviderApprovedEvent(ServiceProviderApprovedEvent event);

  void serviceProviderCreatedEvent(ServiceProviderCreatedEvent event);

  void serviceProviderRejectedEvent(ServiceProviderRejectedEvent event);

  void serviceProviderServiceAddedEvent(ServiceProviderServiceAddedEvent event);

  void serviceProviderPortfolioItemAddedEvent(ServiceProviderPortfolioItemAddedEvent event);

  void serviceProviderPortfolioItemUpdatedEvent(ServiceProviderPortfolioItemUpdatedEvent event);

  void serviceProviderPortfolioItemDeletedEvent(ServiceProviderPortfolioItemDeletedEvent event);

  void serviceProviderProfileUpdatedEvent(ServiceProviderProfileUpdatedEvent event);
}
