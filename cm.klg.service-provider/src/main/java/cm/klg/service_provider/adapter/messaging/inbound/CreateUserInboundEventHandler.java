package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;

public record CreateUserInboundEventHandler(
    CreateNewUserUseCase createNewUserUseCase, MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<UamUserCreatedEventDTO> {
  @Override
  public String handledEventType() {
    return UamDomainEventType.USER_CREATED.getValue();
  }

  @Override
  public Class<UamUserCreatedEventDTO> payloadType() {
    return UamUserCreatedEventDTO.class;
  }

  @Override
  public void handle(InboxEventCommand<UamUserCreatedEventDTO> inboxEventCommand) {
    createNewUserUseCase.execute(
        messagingInboundMapper.toCreateUserCommand(inboxEventCommand.data()));
  }
}
