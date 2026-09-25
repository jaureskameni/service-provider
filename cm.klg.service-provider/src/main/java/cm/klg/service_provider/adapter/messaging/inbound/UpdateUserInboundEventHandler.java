package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserUpdatedEventDTO;
import cm.klg.service_provider.application.usecase.UpdateUserUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;

public record UpdateUserInboundEventHandler(
    UpdateUserUseCase updateUserUseCase, MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<UamUserUpdatedEventDTO> {
  @Override
  public String handledEventType() {
    return UamDomainEventType.USER_UPDATED.getValue();
  }

  @Override
  public Class<UamUserUpdatedEventDTO> payloadType() {
    return UamUserUpdatedEventDTO.class;
  }

  @Override
  public void handle(InboxEventCommand<UamUserUpdatedEventDTO> inboxEventCommand) {
    updateUserUseCase.execute(messagingInboundMapper.toUpdateUserCommand(inboxEventCommand.data()));
  }
}
