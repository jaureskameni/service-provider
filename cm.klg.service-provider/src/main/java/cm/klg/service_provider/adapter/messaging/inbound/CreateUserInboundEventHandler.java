package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.DomainEventType;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UserCreatedEventDTO;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;

public record CreateUserInboundEventHandler(
    CreateNewUserUseCase createNewUserUseCase,
    MessagingInboundMapper messagingInboundMapper,
    UseCaseExecutor useCaseExecutor)
    implements InboxEventHandler<UserCreatedEventDTO> {
  @Override
  public String getEventType() {
    return DomainEventType.USER_CREATED.getValue();
  }

  @Override
  public Class<UserCreatedEventDTO> getDataType() {
    return UserCreatedEventDTO.class;
  }

  @Override
  public void handle(UserCreatedEventDTO userCreatedEventDTO, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createNewUserUseCase.execute(
                messagingInboundMapper.toCreateUserCommand(userCreatedEventDTO)));
  }
}
