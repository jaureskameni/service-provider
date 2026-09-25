package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.service_provider.application.usecase.DeleteUserUseCase;
import cm.klg.service_provider.domain.UserId;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.Objects;

public record DeleteUserInboundEventHandler(
    DeleteUserUseCase deleteUserUseCase, MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<UamUserDeletedEventDTO> {
  @Override
  public String handledEventType() {
    return UamDomainEventType.USER_DELETED.getValue();
  }

  @Override
  public Class<UamUserDeletedEventDTO> payloadType() {
    return UamUserDeletedEventDTO.class;
  }

  @Override
  public void handle(InboxEventCommand<UamUserDeletedEventDTO> inboxEventCommand) {
    deleteUserUseCase.execute(
        UserId.from(Objects.requireNonNull(inboxEventCommand.data().getId())));
  }
}
