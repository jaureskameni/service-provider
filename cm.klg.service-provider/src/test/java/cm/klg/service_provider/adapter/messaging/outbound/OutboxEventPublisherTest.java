package cm.klg.service_provider.adapter.messaging.outbound;

import static cm.klg.service_provider.adapter.messaging.outbound.EventTopics.DESTINATION_SERVICE_PROVIDER_OUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.DomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderApprovedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderCreatedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderRejectedEventDTO;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderCreatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import com.emb.application.outbound.EventPublisher;
import com.emb.domain.outboxevent.EventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxEventPublisherTest {

  @Mock private EventPublisher eventPublisher;
  @Mock private OutboxPublisherMapper outboxPublisherMapper;
  @InjectMocks private OutboxEventPublisher objectUnderTest;

  @Test
  void serviceProviderApprovedEvent_shouldPublishOutboxEvent() {
    // Given
    UUID spId = UUID.randomUUID();
    ServiceProviderApprovedEvent event = mock(ServiceProviderApprovedEvent.class);
    when(event.serviceProviderId()).thenReturn(new ServiceProviderId(spId));

    ServiceProviderApprovedEventDTO dto = new ServiceProviderApprovedEventDTO();
    when(outboxPublisherMapper.toServiceProviderApprovedEventDTO(event)).thenReturn(dto);

    // When
    objectUnderTest.serviceProviderApprovedEvent(event);

    // Then
    ArgumentCaptor<EventCommand> captor = ArgumentCaptor.forClass(EventCommand.class);
    verify(eventPublisher).publish(captor.capture());

    EventCommand capturedCommand = captor.getValue();
    assertThat(capturedCommand.topic()).isEqualTo(DESTINATION_SERVICE_PROVIDER_OUT);
    assertThat(capturedCommand.type()).isEqualTo(DomainEventType.SERVICE_PROVIDER_APPROVED.name());
    assertThat(capturedCommand.key()).isEqualTo(spId.toString());
    assertThat(capturedCommand.data()).isEqualTo(dto);
  }

  @Test
  void serviceProviderCreatedEvent_shouldPublishOutboxEvent() {
    // Given
    UUID spId = UUID.randomUUID();
    ServiceProviderCreatedEvent event = mock(ServiceProviderCreatedEvent.class);
    when(event.serviceProviderId()).thenReturn(new ServiceProviderId(spId));

    ServiceProviderCreatedEventDTO dto = new ServiceProviderCreatedEventDTO();
    when(outboxPublisherMapper.toServiceProviderCreatedEventDTO(event)).thenReturn(dto);

    // When
    objectUnderTest.serviceProviderCreatedEvent(event);

    // Then
    ArgumentCaptor<EventCommand> captor = ArgumentCaptor.forClass(EventCommand.class);
    verify(eventPublisher).publish(captor.capture());

    EventCommand capturedCommand = captor.getValue();
    assertThat(capturedCommand.topic()).isEqualTo(DESTINATION_SERVICE_PROVIDER_OUT);
    assertThat(capturedCommand.type()).isEqualTo(DomainEventType.SERVICE_PROVIDER_CREATED.name());
    assertThat(capturedCommand.key()).isEqualTo(spId.toString());
    assertThat(capturedCommand.data()).isEqualTo(dto);
  }

  @Test
  void serviceProviderRejectedEvent_shouldPublishOutboxEvent() {
    // Given
    UUID spId = UUID.randomUUID();
    ServiceProviderRejectedEvent event = mock(ServiceProviderRejectedEvent.class);
    when(event.serviceProviderId()).thenReturn(new ServiceProviderId(spId));

    ServiceProviderRejectedEventDTO dto = new ServiceProviderRejectedEventDTO();
    when(outboxPublisherMapper.toServiceProviderRejectedEventDTO(event)).thenReturn(dto);

    // When
    objectUnderTest.serviceProviderRejectedEvent(event);

    // Then
    ArgumentCaptor<EventCommand> captor = ArgumentCaptor.forClass(EventCommand.class);
    verify(eventPublisher).publish(captor.capture());

    EventCommand capturedCommand = captor.getValue();
    assertThat(capturedCommand.topic()).isEqualTo(DESTINATION_SERVICE_PROVIDER_OUT);
    assertThat(capturedCommand.type()).isEqualTo(DomainEventType.SERVICE_PROVIDER_REJECTED.name());
    assertThat(capturedCommand.key()).isEqualTo(spId.toString());
    assertThat(capturedCommand.data()).isEqualTo(dto);
  }
}
