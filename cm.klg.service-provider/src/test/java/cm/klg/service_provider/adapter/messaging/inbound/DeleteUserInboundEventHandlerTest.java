package cm.klg.service_provider.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.service_provider.application.usecase.DeleteUserUseCase;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserInboundEventHandlerTest {

  @Mock private DeleteUserUseCase deleteUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;
  @Mock private UseCaseExecutor useCaseExecutor;

  @InjectMocks private DeleteUserInboundEventHandler deleteUserInboundEventHandler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(deleteUserInboundEventHandler.getEventType())
        .isEqualTo(UamDomainEventType.USER_DELETED.getValue());
  }

  @Test
  void shouldReturnCorrectDataType() {
    assertThat(deleteUserInboundEventHandler.getDataType()).isEqualTo(UamUserDeletedEventDTO.class);
  }

  @Test
  void shouldHandleUserDeletedEvent() {
    // Given
    UamUserDeletedEventDTO userDeletedEventDTO = mock(UamUserDeletedEventDTO.class);
    InboxEventCommand inboxEventCommand = mock(InboxEventCommand.class);

    // When
    deleteUserInboundEventHandler.handle(userDeletedEventDTO, inboxEventCommand);

    // Then
    verify(useCaseExecutor).runCommand(any());
  }
}
