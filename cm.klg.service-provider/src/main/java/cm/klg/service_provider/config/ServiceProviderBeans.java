package cm.klg.service_provider.config;

import cm.klg.common.base.config.TransactionBeansProvider;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_provider.adapter.rest.inbound.DefaultDomainToHttpExceptionTranslator;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class ServiceProviderBeans implements TransactionBeansProvider {

  @Bean
  public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    return new JwtGrantedAuthoritiesConverter();
  }

  @Bean
  public DomainToHttpExceptionTranslator domainToHttpExceptionTranslator() {
    return new DefaultDomainToHttpExceptionTranslator();
  }

  @Bean
  public CreateNewUserUseCase createNewUserUseCase(UserRepository userRepository) {
    return new CreateNewUserUseCase(userRepository);
  }

  @Bean
  public BecomeServiceProviderUseCase becomeServiceProviderUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new BecomeServiceProviderUseCase(serviceProviderRepository);
  }

  @Bean
  public ApproveServiceProviderRequestUseCase approveServiceProviderRequestUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new ApproveServiceProviderRequestUseCase(serviceProviderRepository);
  }
}
