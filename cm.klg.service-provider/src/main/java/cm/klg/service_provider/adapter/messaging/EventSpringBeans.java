package cm.klg.service_provider.adapter.messaging;

import cm.klg.service_provider.adapter.messaging.outbound.OutboxEventPublisher;
import cm.klg.service_provider.adapter.messaging.outbound.OutboxPublisherMapper;
import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import com.emb.application.outbound.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventSpringBeans {

  @Bean
  public DomainEventPublisher domainEventPublisher(
      EventPublisher eventPublisher, OutboxPublisherMapper outboxPublisherMapper) {
    return new OutboxEventPublisher(eventPublisher, outboxPublisherMapper);
  }
}
