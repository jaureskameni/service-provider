package cm.klg.service_provider.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserUpdatedEventDTO;
import cm.klg.service_provider.application.usecase.UpdateUserUseCase;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserInboundEventHandlerTest {

  @Mock private UpdateUserUseCase updateUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private UpdateUserInboundEventHandler updateUserInboundEventHandler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(updateUserInboundEventHandler.getEventType())
        .isEqualTo(UamDomainEventType.USER_UPDATED.getValue());
  }

  @Test
  void shouldReturnCorrectDataType() {
    assertThat(updateUserInboundEventHandler.getDataType()).isEqualTo(UamUserUpdatedEventDTO.class);
  }

  @Test
  void shouldHandleUserUpdatedEvent() {
    // Given
    UamUserUpdatedEventDTO userUpdatedEventDTO = new UamUserUpdatedEventDTO();
    InboxEventCommand inboxEventCommand = mock(InboxEventCommand.class);
    UpdateUserUseCase.UpdateUserCommand command =
        new UpdateUserUseCase.UpdateUserCommand(
            UUID.randomUUID(), "Smith", "Jane", "jane@doe.com", "237", "699");

    when(messagingInboundMapper.toUpdateUserCommand(userUpdatedEventDTO)).thenReturn(command);

    // When
    updateUserInboundEventHandler.handle(userUpdatedEventDTO, inboxEventCommand);

    // Then
    verify(updateUserUseCase).execute(command);
  }
}
