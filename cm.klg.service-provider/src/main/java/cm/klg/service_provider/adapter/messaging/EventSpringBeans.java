package cm.klg.service_provider.adapter.messaging;

import cm.klg.service_provider.adapter.messaging.outbound.EventPublisher;
import cm.klg.service_provider.adapter.messaging.outbound.OutboxPublisherMapper;
import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventSpringBeans {

  @Bean
  public DomainEventPublisher domainEventPublisher(
      com.emb.application.outbound.EventPublisher eventPublisher,
      OutboxPublisherMapper outboxPublisherMapper) {
    return new EventPublisher(eventPublisher, outboxPublisherMapper);
  }
}
