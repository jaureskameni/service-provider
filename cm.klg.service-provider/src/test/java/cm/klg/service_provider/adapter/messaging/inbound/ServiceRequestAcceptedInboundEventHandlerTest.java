package cm.klg.service_provider.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.usecase.CreateNewProviderClientUseCase;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.SRDomainEventType;
import org.openapitools.model.SRServiceRequestAcceptedEventDTO;

@ExtendWith(MockitoExtension.class)
class ServiceRequestAcceptedInboundEventHandlerTest {

  @Mock private CreateNewProviderClientUseCase createNewProviderClientUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private ServiceRequestAcceptedInboundEventHandler handler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(handler.handledEventType())
        .isEqualTo(SRDomainEventType.SERVICE_REQUEST_ACCEPTED.getValue());
  }

  @Test
  void shouldReturnCorrectDataType() {
    assertThat(handler.payloadType()).isEqualTo(SRServiceRequestAcceptedEventDTO.class);
  }

  @Test
  void shouldHandleServiceRequestAcceptedEvent() {
    // Given
    SRServiceRequestAcceptedEventDTO eventDTO = new SRServiceRequestAcceptedEventDTO();
    InboxEventCommand<SRServiceRequestAcceptedEventDTO> inboxEventCommand =
        mock(InboxEventCommand.class);
    CreateNewProviderClientUseCase.Command command =
        new CreateNewProviderClientUseCase.Command(
            new ServiceProviderId(UUID.randomUUID()),
            new UserId(UUID.randomUUID()),
            new CreatedAt(LocalDateTime.now()));

    when(messagingInboundMapper.toCreateProviderClientCommand(eventDTO)).thenReturn(command);
    when(inboxEventCommand.data()).thenReturn(eventDTO);

    // When
    handler.handle(inboxEventCommand);

    // Then
    verify(createNewProviderClientUseCase).execute(command);
  }
}
