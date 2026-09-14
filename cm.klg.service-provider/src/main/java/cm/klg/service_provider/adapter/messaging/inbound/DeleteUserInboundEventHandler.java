package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.service_provider.application.usecase.DeleteUserUseCase;
import cm.klg.service_provider.domain.UserId;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.Objects;

public record DeleteUserInboundEventHandler(
    DeleteUserUseCase deleteUserUseCase,
    MessagingInboundMapper messagingInboundMapper,
    UseCaseExecutor useCaseExecutor)
    implements InboxEventHandler<UamUserDeletedEventDTO> {
  @Override
  public String getEventType() {
    return UamDomainEventType.USER_DELETED.getValue();
  }

  @Override
  public Class<UamUserDeletedEventDTO> getDataType() {
    return UamUserDeletedEventDTO.class;
  }

  @Override
  public void handle(
      UamUserDeletedEventDTO userDeletedEventDTO, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            deleteUserUseCase.execute(
                UserId.from(Objects.requireNonNull(userDeletedEventDTO.getUserId()))));
  }
}
