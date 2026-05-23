package cm.klg.service_provider.adapter.messaging.outbound;

import static cm.klg.service_provider.adapter.messaging.outbound.EventTopics.DESTINATION_SERVICE_PROVIDER_OUT;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.DomainEventType;
import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import com.emb.application.outbound.OutboxWriter;
import com.emb.domain.outboxevent.OutboxEventCommand;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class OutboxWriterDomainEventPublisher implements DomainEventPublisher {
  private final OutboxWriter outboxWriter;
  private final OutboxWriterMapper outboxWriterMapper;

  @Override
  public void serviceProviderApprovedEvent(@NonNull ServiceProviderApprovedEvent event) {
    outboxWriter.publish(
        new OutboxEventCommand(
            DESTINATION_SERVICE_PROVIDER_OUT,
            DomainEventType.SERVICE_PROVIDER_APPROVED.name(),
            event.serviceProviderId().value().toString(),
            outboxWriterMapper.toServiceProviderApprovedEventDTO(event)));
  }
}
