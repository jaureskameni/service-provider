package cm.klg.service_provider.config;

import cm.klg.common.base.config.TransactionBeansProvider;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_provider.adapter.rest.inbound.DefaultDomainToHttpExceptionTranslator;
import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.AddPortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.CreateNewProviderClientUseCase;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceTypesUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderProfileUseCase;
import cm.klg.service_provider.application.usecase.RejectServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class ServiceProviderBeans implements TransactionBeansProvider {

  @Bean
  @Override
  public DomainToHttpExceptionTranslator domainToHttpExceptionTranslator() {
    return new DefaultDomainToHttpExceptionTranslator();
  }

  @Bean
  public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    return new JwtGrantedAuthoritiesConverter();
  }

  @Bean
  public BecomeServiceProviderUseCase becomeServiceProviderUseCase(
      ServiceProviderRepository serviceProviderRepository,
      DomainEventPublisher domainEventPublisher,
      UserRepository userRepository,
      ServiceTypeRepository serviceTypeRepository) {
    return new BecomeServiceProviderUseCase(
        serviceProviderRepository, userRepository, serviceTypeRepository, domainEventPublisher);
  }

  @Bean
  public GetAllServiceProviderUseCase getAllServiceProviderUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new GetAllServiceProviderUseCase(serviceProviderRepository);
  }

  @Bean
  public SearchServiceProviderUseCase searchServiceProviderUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new SearchServiceProviderUseCase(serviceProviderRepository);
  }

  @Bean
  public GetServiceProviderByIdUseCase getServiceProviderByIdUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new GetServiceProviderByIdUseCase(serviceProviderRepository);
  }

  @Bean
  public ApproveServiceProviderRequestUseCase approveServiceProviderRequestUseCase(
      ServiceProviderRepository serviceProviderRepository,
      UserRepository userRepository,
      DomainEventPublisher domainEventPublisher) {
    return new ApproveServiceProviderRequestUseCase(
        serviceProviderRepository, userRepository, domainEventPublisher);
  }

  @Bean
  public RejectServiceProviderRequestUseCase rejectServiceProviderRequestUseCase(
      ServiceProviderRepository serviceProviderRepository,
      DomainEventPublisher domainEventPublisher) {
    return new RejectServiceProviderRequestUseCase(serviceProviderRepository, domainEventPublisher);
  }

  @Bean
  public AddPortfolioItemUseCase addPortfolioItemUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new AddPortfolioItemUseCase(serviceProviderRepository);
  }

  @Bean
  public AddNewServiceUseCase addNewServiceUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ServiceTypeRepository serviceTypeRepository) {
    return new AddNewServiceUseCase(serviceProviderRepository, serviceTypeRepository);
  }

  @Bean
  public CreateNewUserUseCase createNewUserUseCase(UserRepository userRepository) {
    return new CreateNewUserUseCase(userRepository);
  }

  @Bean
  public GetAllServiceTypesUseCase getAllServiceTypesUseCase(
      ServiceTypeRepository serviceTypeRepository) {
    return new GetAllServiceTypesUseCase(serviceTypeRepository);
  }

  @Bean
  public CreateNewProviderClientUseCase createNewProviderClientUseCase(
      ProviderClientRepository providerClientRepository) {
    return new CreateNewProviderClientUseCase(providerClientRepository);
  }

  @Bean
  public GetServiceProviderProfileUseCase getPublicServiceProviderProfileUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ProviderClientRepository providerClientRepository) {
    return new GetServiceProviderProfileUseCase(
        serviceProviderRepository, providerClientRepository);
  }
}
