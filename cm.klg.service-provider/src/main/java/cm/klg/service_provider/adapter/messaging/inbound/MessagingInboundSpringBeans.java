package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.service_provider.application.usecase.CreateNewProviderClientUseCase;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingInboundSpringBeans {

  @Bean
  public CreateUserInboundEventHandler createUserInboundEventHandler(
      CreateNewUserUseCase createNewUserUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new CreateUserInboundEventHandler(
        createNewUserUseCase, messagingInboundMapper, useCaseExecutor);
  }

  @Bean
  public ServiceRequestAcceptedInboundEventHandler serviceRequestAcceptedInboundEventHandler(
      CreateNewProviderClientUseCase createNewProviderClientUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new ServiceRequestAcceptedInboundEventHandler(
        createNewProviderClientUseCase, messagingInboundMapper, useCaseExecutor);
  }
}
