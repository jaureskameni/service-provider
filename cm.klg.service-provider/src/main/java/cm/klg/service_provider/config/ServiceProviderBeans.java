package cm.klg.service_provider.config;

import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_provider.adapter.rest.inbound.DefaultDomainToHttpExceptionTranslator;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class ServiceProviderBeans {

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
}
