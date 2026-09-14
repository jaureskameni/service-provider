package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserUpdatedEventDTO;
import cm.klg.service_provider.application.usecase.UpdateUserUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;

public record UpdateUserInboundEventHandler(
    UpdateUserUseCase updateUserUseCase,
    MessagingInboundMapper messagingInboundMapper,
    UseCaseExecutor useCaseExecutor)
    implements InboxEventHandler<UamUserUpdatedEventDTO> {
  @Override
  public String getEventType() {
    return UamDomainEventType.USER_UPDATED.getValue();
  }

  @Override
  public Class<UamUserUpdatedEventDTO> getDataType() {
    return UamUserUpdatedEventDTO.class;
  }

  @Override
  public void handle(
      UamUserUpdatedEventDTO userUpdatedEventDTO, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            updateUserUseCase.execute(
                messagingInboundMapper.toUpdateUserCommand(userUpdatedEventDTO)));
  }
}
