package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.service_provider.application.usecase.CreateNewProviderClientUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.SRDomainEventType;
import org.openapitools.model.SRServiceRequestAcceptedEventDTO;

public record ServiceRequestAcceptedInboundEventHandler(
    CreateNewProviderClientUseCase createNewProviderClientUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<SRServiceRequestAcceptedEventDTO> {
  @Override
  public String handledEventType() {
    return SRDomainEventType.SERVICE_REQUEST_ACCEPTED.getValue();
  }

  @Override
  public Class<SRServiceRequestAcceptedEventDTO> payloadType() {
    return SRServiceRequestAcceptedEventDTO.class;
  }

  @Override
  public void handle(InboxEventCommand<SRServiceRequestAcceptedEventDTO> inboxEventCommand) {
    createNewProviderClientUseCase.execute(
        messagingInboundMapper.toCreateProviderClientCommand(inboxEventCommand.data()));
  }
}
