package cm.klg.service_provider.adapter.messaging.inbound;

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
      CreateNewUserUseCase createNewUserUseCase, MessagingInboundMapper messagingInboundMapper) {
    return new CreateUserInboundEventHandler(createNewUserUseCase, messagingInboundMapper);
  }

  @Bean
  public UpdateUserInboundEventHandler updateUserInboundEventHandler(
      UpdateUserUseCase updateUserUseCase, MessagingInboundMapper messagingInboundMapper) {
    return new UpdateUserInboundEventHandler(updateUserUseCase, messagingInboundMapper);
  }

  @Bean
  public DeleteUserInboundEventHandler deleteUserInboundEventHandler(
      DeleteUserUseCase deleteUserUseCase, MessagingInboundMapper messagingInboundMapper) {
    return new DeleteUserInboundEventHandler(deleteUserUseCase, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestAcceptedInboundEventHandler serviceRequestAcceptedInboundEventHandler(
      CreateNewProviderClientUseCase createNewProviderClientUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new ServiceRequestAcceptedInboundEventHandler(
        createNewProviderClientUseCase, messagingInboundMapper);
  }
}
