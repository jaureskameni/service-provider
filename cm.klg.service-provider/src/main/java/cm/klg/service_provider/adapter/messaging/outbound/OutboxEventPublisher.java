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
import com.emb.application.outbound.EventPublisher;
import com.emb.domain.outboxevent.EventCommand;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class OutboxEventPublisher implements DomainEventPublisher {
  private final EventPublisher outboxEventSender;
  private final OutboxPublisherMapper outboxPublisherMapper;

  @Override
  public void serviceProviderApprovedEvent(@NonNull ServiceProviderApprovedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_APPROVED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderApprovedEventDTO(event));
  }

  @Override
  public void serviceProviderCreatedEvent(@NonNull ServiceProviderCreatedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_CREATED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderCreatedEventDTO(event));
  }

  @Override
  public void serviceProviderRejectedEvent(@NonNull ServiceProviderRejectedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_REJECTED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderRejectedEventDTO(event));
  }

  @Override
  public void serviceProviderServiceAddedEvent(@NonNull ServiceProviderServiceAddedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_SERVICE_ADDED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderServiceAddedEventDTO(event));
  }

  @Override
  public void serviceProviderPortfolioItemAddedEvent(
      @NonNull ServiceProviderPortfolioItemAddedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PORTFOLIO_ITEM_ADDED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderPortfolioItemAddedEventDTO(event));
  }

  @Override
  public void serviceProviderPortfolioItemUpdatedEvent(
      @NonNull ServiceProviderPortfolioItemUpdatedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PORTFOLIO_ITEM_UPDATED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderPortfolioItemUpdatedEventDTO(event));
  }

  @Override
  public void serviceProviderPortfolioItemDeletedEvent(
      @NonNull ServiceProviderPortfolioItemDeletedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PORTFOLIO_ITEM_DELETED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderPortfolioItemDeletedEventDTO(event));
  }

  @Override
  public void serviceProviderProfileUpdatedEvent(
      @NonNull ServiceProviderProfileUpdatedEvent event) {
    publish(
        DomainEventType.SERVICE_PROVIDER_PROFILE_UPDATED,
        event.serviceProviderId().value().toString(),
        outboxPublisherMapper.toServiceProviderProfileUpdatedEventDTO(event));
  }

  private void publish(DomainEventType type, String key, Object data) {
    outboxEventSender.publish(
        new EventCommand(DESTINATION_SERVICE_PROVIDER_OUT, type.name(), key, data));
  }
}
