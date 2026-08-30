package cm.klg.service_provider.adapter.messaging.outbound;

import static cm.klg.service_provider.adapter.messaging.outbound.EventTopics.DESTINATION_SERVICE_PROVIDER_OUT;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.DomainEventType;
import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderCreatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemAddedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemDeletedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemUpdatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileUpdatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderServiceAddedEvent;
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

  @Override
  public void serviceProviderCreatedEvent(@NonNull ServiceProviderCreatedEvent event) {
    outboxWriter.publish(
        new OutboxEventCommand(
            DESTINATION_SERVICE_PROVIDER_OUT,
            DomainEventType.SERVICE_PROVIDER_CREATED.name(),
            event.serviceProviderId().value().toString(),
            outboxWriterMapper.toServiceProviderCreatedEventDTO(event)));
  }

  @Override
  public void serviceProviderRejectedEvent(@NonNull ServiceProviderRejectedEvent event) {
    outboxWriter.publish(
        new OutboxEventCommand(
            DESTINATION_SERVICE_PROVIDER_OUT,
            DomainEventType.SERVICE_PROVIDER_REJECTED.name(),
            event.serviceProviderId().value().toString(),
            outboxWriterMapper.toServiceProviderRejectedEventDTO(event)));
  }

  @Override
  public void serviceProviderServiceAddedEvent(@NonNull ServiceProviderServiceAddedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_SERVICE_ADDED,
        event.serviceProviderId().value().toString(),
        outboxWriterMapper.toServiceProviderServiceAddedEventDTO(event));
  }

  @Override
  public void serviceProviderPortfolioItemAddedEvent(
      @NonNull ServiceProviderPortfolioItemAddedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PORTFOLIO_ITEM_ADDED,
        event.serviceProviderId().value().toString(),
        outboxWriterMapper.toServiceProviderPortfolioItemAddedEventDTO(event));
  }

  @Override
  public void serviceProviderPortfolioItemUpdatedEvent(
      @NonNull ServiceProviderPortfolioItemUpdatedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PORTFOLIO_ITEM_UPDATED,
        event.serviceProviderId().value().toString(),
        outboxWriterMapper.toServiceProviderPortfolioItemUpdatedEventDTO(event));
  }

  @Override
  public void serviceProviderPortfolioItemDeletedEvent(
      @NonNull ServiceProviderPortfolioItemDeletedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PORTFOLIO_ITEM_DELETED,
        event.serviceProviderId().value().toString(),
        outboxWriterMapper.toServiceProviderPortfolioItemDeletedEventDTO(event));
  }

  @Override
  public void serviceProviderProfileUpdatedEvent(
      @NonNull ServiceProviderProfileUpdatedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PROFILE_UPDATED,
        event.serviceProviderId().value().toString(),
        outboxWriterMapper.toServiceProviderProfileUpdatedEventDTO(event));
  }

  private void publish(DomainEventType type, String key, Object data) {
    outboxWriter.publish(
        new OutboxEventCommand(DESTINATION_SERVICE_PROVIDER_OUT, type.name(), key, data));
  }
}
