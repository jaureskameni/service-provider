package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileImageApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileImageRejectedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;

public interface DomainEventPublisher {
  void serviceProviderApprovedEvent(ServiceProviderApprovedEvent event);

  void serviceProviderRejectedEvent(ServiceProviderRejectedEvent event);

  void serviceProviderProfileImageApprovedEvent(ServiceProviderProfileImageApprovedEvent event);

  void serviceProviderProfileImageRejectedEvent(ServiceProviderProfileImageRejectedEvent event);
}
