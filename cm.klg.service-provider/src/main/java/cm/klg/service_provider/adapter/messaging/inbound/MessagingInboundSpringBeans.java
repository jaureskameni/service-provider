package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.service_provider.application.usecase.CreateNewProviderClientUseCase;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import cm.klg.service_provider.application.usecase.DeleteUserUseCase;
import cm.klg.service_provider.application.usecase.UpdateUserUseCase;
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
  public UpdateUserInboundEventHandler updateUserInboundEventHandler(
      UpdateUserUseCase updateUserUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new UpdateUserInboundEventHandler(
        updateUserUseCase, messagingInboundMapper, useCaseExecutor);
  }

  @Bean
  public DeleteUserInboundEventHandler deleteUserInboundEventHandler(
      DeleteUserUseCase deleteUserUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new DeleteUserInboundEventHandler(
        deleteUserUseCase, messagingInboundMapper, useCaseExecutor);
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
