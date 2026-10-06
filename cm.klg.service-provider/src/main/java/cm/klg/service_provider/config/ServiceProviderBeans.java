package cm.klg.service_provider.config;

import cm.klg.common.base.config.TransactionBeansProvider;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_provider.adapter.rest.inbound.DefaultDomainToHttpExceptionTranslator;
import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.AddPortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.AddServiceProviderToFavoritesUseCase;
import cm.klg.service_provider.application.usecase.ApproveProfileImageUseCase;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.CreateNewProviderClientUseCase;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import cm.klg.service_provider.application.usecase.DeletePortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.DeleteProviderServiceUseCase;
import cm.klg.service_provider.application.usecase.DeleteUserUseCase;
import cm.klg.service_provider.application.usecase.GetAllMyPortfolioUseCase;
import cm.klg.service_provider.application.usecase.GetAllMyServicesUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceTypesUseCase;
import cm.klg.service_provider.application.usecase.GetMyFavoriteServiceProvidersUseCase;
import cm.klg.service_provider.application.usecase.GetMyServiceProviderIdentityUseCase;
import cm.klg.service_provider.application.usecase.GetMyServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetProfileImageReviewUseCase;
import cm.klg.service_provider.application.usecase.GetProfileImageReviewsUseCase;
import cm.klg.service_provider.application.usecase.GetProviderPortfolioUseCase;
import cm.klg.service_provider.application.usecase.GetProviderServicesUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderVerificationUseCase;
import cm.klg.service_provider.application.usecase.RejectProfileImageUseCase;
import cm.klg.service_provider.application.usecase.RejectServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.RemoveServiceProviderFromFavoritesUseCase;
import cm.klg.service_provider.application.usecase.ResubmitRejectedServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdateApprovedServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdatePortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.UpdateUserUseCase;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
@EnableCaching
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
      UserRepository userRepository,
      ServiceTypeRepository serviceTypeRepository) {
    return new BecomeServiceProviderUseCase(
        serviceProviderRepository, userRepository, serviceTypeRepository);
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
      ServiceProviderRepository serviceProviderRepository,
      ProviderClientRepository providerClientRepository,
      FavoriteProviderRepository favoriteProviderRepository) {
    return new GetServiceProviderByIdUseCase(
        serviceProviderRepository, providerClientRepository, favoriteProviderRepository);
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
  public UpdateUserUseCase updateUserUseCase(UserRepository userRepository) {
    return new UpdateUserUseCase(userRepository);
  }

  @Bean
  public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
    return new DeleteUserUseCase(userRepository);
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
  public GetMyServiceProviderUseCase getMyServiceProviderUseCase(
      ServiceProviderRepository repository) {
    return new GetMyServiceProviderUseCase(repository);
  }

  @Bean
  public GetMyServiceProviderIdentityUseCase getMyServiceProviderIdentityUseCase(
      ServiceProviderRepository repository) {
    return new GetMyServiceProviderIdentityUseCase(repository);
  }

  @Bean
  public GetServiceProviderVerificationUseCase getServiceProviderVerificationUseCase(
      ServiceProviderRepository repository) {
    return new GetServiceProviderVerificationUseCase(repository);
  }

  @Bean
  public GetProfileImageReviewsUseCase getProfileImageReviewsUseCase(
      ServiceProviderRepository repository) {
    return new GetProfileImageReviewsUseCase(repository);
  }

  @Bean
  public GetProfileImageReviewUseCase getProfileImageReviewUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new GetProfileImageReviewUseCase(serviceProviderRepository);
  }

  @Bean
  public ResubmitRejectedServiceProviderUseCase resubmitRejectedServiceProviderUseCase(
      ServiceProviderRepository providerRepository, UserRepository userRepository) {
    return new ResubmitRejectedServiceProviderUseCase(providerRepository, userRepository);
  }

  @Bean
  public UpdateApprovedServiceProviderUseCase updateApprovedServiceProviderUseCase(
      ServiceProviderRepository providerRepository, UserRepository userRepository) {
    return new UpdateApprovedServiceProviderUseCase(providerRepository, userRepository);
  }

  @Bean
  public ApproveProfileImageUseCase approveProfileImageUseCase(
      ServiceProviderRepository repository, DomainEventPublisher events) {
    return new ApproveProfileImageUseCase(repository, events);
  }

  @Bean
  public RejectProfileImageUseCase rejectProfileImageUseCase(
      ServiceProviderRepository repository, DomainEventPublisher events) {
    return new RejectProfileImageUseCase(repository, events);
  }

  @Bean
  public DeleteProviderServiceUseCase deleteProviderServiceUseCase(
      ServiceProviderRepository repository) {
    return new DeleteProviderServiceUseCase(repository);
  }

  @Bean
  public GetAllMyPortfolioUseCase getAllMyPortfolioUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new GetAllMyPortfolioUseCase(serviceProviderRepository);
  }

  @Bean
  public GetProviderPortfolioUseCase getProviderPortfolioUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new GetProviderPortfolioUseCase(serviceProviderRepository);
  }

  @Bean
  public UpdatePortfolioItemUseCase updatePortfolioItemUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new UpdatePortfolioItemUseCase(serviceProviderRepository);
  }

  @Bean
  public DeletePortfolioItemUseCase deletePortfolioItemUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new DeletePortfolioItemUseCase(serviceProviderRepository);
  }

  @Bean
  public GetAllMyServicesUseCase getAllMyServicesUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new GetAllMyServicesUseCase(serviceProviderRepository);
  }

  @Bean
  public GetProviderServicesUseCase getProviderServicesUseCase(
      ServiceProviderRepository serviceProviderRepository) {
    return new GetProviderServicesUseCase(serviceProviderRepository);
  }

  @Bean
  public AddServiceProviderToFavoritesUseCase addServiceProviderToFavoritesUseCase(
      FavoriteProviderRepository favoriteProviderRepository,
      ServiceProviderRepository serviceProviderRepository) {
    return new AddServiceProviderToFavoritesUseCase(
        favoriteProviderRepository, serviceProviderRepository);
  }

  @Bean
  public RemoveServiceProviderFromFavoritesUseCase removeServiceProviderFromFavoritesUseCase(
      FavoriteProviderRepository favoriteProviderRepository) {
    return new RemoveServiceProviderFromFavoritesUseCase(favoriteProviderRepository);
  }

  @Bean
  public GetMyFavoriteServiceProvidersUseCase getMyFavoriteServiceProvidersUseCase(
      FavoriteProviderRepository favoriteProviderRepository) {
    return new GetMyFavoriteServiceProvidersUseCase(favoriteProviderRepository);
  }
}
