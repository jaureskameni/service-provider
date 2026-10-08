package cm.klg.service_provider.adapter.messaging.outbound;

import static cm.klg.service_provider.adapter.messaging.outbound.EventTopics.DESTINATION_SERVICE_PROVIDER_OUT;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.DomainEventType;
import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileImageApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileImageRejectedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import com.emb.domain.outboxevent.EventCommand;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class EventPublisher implements DomainEventPublisher {
  private final com.emb.application.outbound.EventPublisher outboxEventSender;
  private final OutboxPublisherMapper outboxPublisherMapper;

  @Override
  public void serviceProviderApprovedEvent(@NonNull ServiceProviderApprovedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_APPROVED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderApprovedEventDTO(event));
  }

  @Override
  public void serviceProviderRejectedEvent(@NonNull ServiceProviderRejectedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_REJECTED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderRejectedEventDTO(event));
  }

  @Override
  public void serviceProviderProfileImageApprovedEvent(
      @NonNull ServiceProviderProfileImageApprovedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PROFILE_IMAGE_APPROVED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderProfileImageApprovedEventDTO(event));
  }

  @Override
  public void serviceProviderProfileImageRejectedEvent(
      @NonNull ServiceProviderProfileImageRejectedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PROFILE_IMAGE_REJECTED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderProfileImageRejectedEventDTO(event));
  }

  private void publish(DomainEventType type, String key, Object data) {
    outboxEventSender.publish(
        new EventCommand(DESTINATION_SERVICE_PROVIDER_OUT, type.name(), key, data));
  }
}
