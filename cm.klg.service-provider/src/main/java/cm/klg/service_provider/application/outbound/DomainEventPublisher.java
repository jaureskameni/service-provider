package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderCreatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;

public interface DomainEventPublisher {
  void serviceProviderApprovedEvent(ServiceProviderApprovedEvent event);

  void serviceProviderCreatedEvent(ServiceProviderCreatedEvent event);

  void serviceProviderRejectedEvent(ServiceProviderRejectedEvent event);
}
