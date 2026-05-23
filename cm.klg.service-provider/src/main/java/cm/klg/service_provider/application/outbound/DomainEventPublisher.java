package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;

public interface DomainEventPublisher {
  void serviceProviderApprovedEvent(ServiceProviderApprovedEvent event);
}
