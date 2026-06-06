package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.service_provider.application.usecase.CreateNewProviderClientUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.SRDomainEventType;
import org.openapitools.model.SRServiceRequestAcceptedEventDTO;

public record ServiceRequestAcceptedInboundEventHandler(
    CreateNewProviderClientUseCase createNewProviderClientUseCase,
    MessagingInboundMapper messagingInboundMapper,
    UseCaseExecutor useCaseExecutor)
    implements InboxEventHandler<SRServiceRequestAcceptedEventDTO> {
  @Override
  public String getEventType() {
    return SRDomainEventType.SERVICE_REQUEST_ACCEPTED.getValue();
  }

  @Override
  public Class<SRServiceRequestAcceptedEventDTO> getDataType() {
    return SRServiceRequestAcceptedEventDTO.class;
  }

  @Override
  public void handle(
      SRServiceRequestAcceptedEventDTO serviceRequestAcceptedEventDTO,
      InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createNewProviderClientUseCase.execute(
                messagingInboundMapper.toCreateProviderClientCommand(
                    serviceRequestAcceptedEventDTO)));
  }
}
