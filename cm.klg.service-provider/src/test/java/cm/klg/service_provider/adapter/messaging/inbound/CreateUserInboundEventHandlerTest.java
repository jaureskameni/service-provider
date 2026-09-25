package cm.klg.service_provider.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserInboundEventHandlerTest {

  @Mock private CreateNewUserUseCase createNewUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private CreateUserInboundEventHandler createUserInboundEventHandler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(createUserInboundEventHandler.handledEventType())
        .isEqualTo(UamDomainEventType.USER_CREATED.getValue());
  }

  @Test
  void shouldReturnCorrectDataType() {
    assertThat(createUserInboundEventHandler.payloadType()).isEqualTo(UamUserCreatedEventDTO.class);
  }

  @Test
  void shouldHandleUserCreatedEvent() {
    // Given
    UamUserCreatedEventDTO userCreatedEventDTO = new UamUserCreatedEventDTO();
    InboxEventCommand<UamUserCreatedEventDTO> inboxEventCommand = mock(InboxEventCommand.class);
    CreateNewUserUseCase.CreateNewUserCommand command =
        new CreateNewUserUseCase.CreateNewUserCommand(
            UUID.randomUUID(), "Doe", "John", "john@doe.com", "237", "699", LocalDateTime.now());

    when(messagingInboundMapper.toCreateUserCommand(userCreatedEventDTO)).thenReturn(command);
    when(inboxEventCommand.data()).thenReturn(userCreatedEventDTO);

    // When
    createUserInboundEventHandler.handle(inboxEventCommand);

    // Then
    verify(createNewUserUseCase).execute(command);
  }
}
