package cm.klg.service_provider.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.service_provider.application.usecase.DeleteUserUseCase;
import cm.klg.service_provider.domain.UserId;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserInboundEventHandlerTest {

  @Mock private DeleteUserUseCase deleteUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private DeleteUserInboundEventHandler deleteUserInboundEventHandler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(deleteUserInboundEventHandler.handledEventType())
        .isEqualTo(UamDomainEventType.USER_DELETED.getValue());
  }

  @Test
  void shouldReturnCorrectDataType() {
    assertThat(deleteUserInboundEventHandler.payloadType()).isEqualTo(UamUserDeletedEventDTO.class);
  }

  @Test
  void shouldHandleUserDeletedEvent() {
    // Given
    UUID userId = UUID.randomUUID();
    UamUserDeletedEventDTO userDeletedEventDTO = mock(UamUserDeletedEventDTO.class);
    when(userDeletedEventDTO.getId()).thenReturn(userId);
    InboxEventCommand<UamUserDeletedEventDTO> inboxEventCommand = mock(InboxEventCommand.class);
    when(inboxEventCommand.data()).thenReturn(userDeletedEventDTO);

    // When
    deleteUserInboundEventHandler.handle(inboxEventCommand);

    // Then
    verify(deleteUserUseCase).execute(UserId.from(userId));
  }
}
