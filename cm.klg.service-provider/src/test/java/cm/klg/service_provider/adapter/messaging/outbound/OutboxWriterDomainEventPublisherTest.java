package cm.klg.service_provider.adapter.messaging.outbound;

import static cm.klg.service_provider.adapter.messaging.outbound.EventTopics.DESTINATION_SERVICE_PROVIDER_OUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.DomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderApprovedEventDTO;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import com.emb.application.outbound.OutboxWriter;
import com.emb.domain.outboxevent.OutboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxWriterDomainEventPublisherTest {

  @Mock private OutboxWriter outboxWriter;
  @Mock private OutboxWriterMapper outboxWriterMapper;
  @InjectMocks private OutboxWriterDomainEventPublisher objectUnderTest;

  @Test
  void serviceProviderApprovedEvent_shouldPublishOutboxEvent() {
    // Given
    UUID spId = UUID.randomUUID();
    ServiceProviderApprovedEvent event = mock(ServiceProviderApprovedEvent.class);
    when(event.serviceProviderId()).thenReturn(new ServiceProviderId(spId));

    ServiceProviderApprovedEventDTO dto = new ServiceProviderApprovedEventDTO();
    when(outboxWriterMapper.toServiceProviderApprovedEventDTO(event)).thenReturn(dto);

    // When
    objectUnderTest.serviceProviderApprovedEvent(event);

    // Then
    ArgumentCaptor<OutboxEventCommand> captor = ArgumentCaptor.forClass(OutboxEventCommand.class);
    verify(outboxWriter).publish(captor.capture());

    OutboxEventCommand capturedCommand = captor.getValue();
    assertThat(capturedCommand.topic()).isEqualTo(DESTINATION_SERVICE_PROVIDER_OUT);
    assertThat(capturedCommand.type()).isEqualTo(DomainEventType.SERVICE_PROVIDER_APPROVED.name());
    assertThat(capturedCommand.key()).isEqualTo(spId.toString());
    assertThat(capturedCommand.data()).isEqualTo(dto);
  }
}
