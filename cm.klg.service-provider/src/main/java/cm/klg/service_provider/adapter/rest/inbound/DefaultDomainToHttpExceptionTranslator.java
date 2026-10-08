package cm.klg.service_provider.adapter.rest.inbound;

import cm.klg.common.base.exception.BadRequestException;
import cm.klg.common.base.exception.ConflictException;
import cm.klg.common.base.exception.HttpErrorException;
import cm.klg.common.base.exception.InternalException;
import cm.klg.common.base.exception.ResourceNotFoundException;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_provider.domain.favorite.CannotFavoriteOwnProfileException;
import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderPaginationDataException;
import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderPhoneNumberException;
import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderStatusException;
import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderYearOfExperienceException;
import cm.klg.service_provider.domain.service_provider.MissingIdentityDocumentsException;
import cm.klg.service_provider.domain.service_provider.PortfolioItemNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderAlreadyProvidesServiceException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderWithPhoneNumberAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.UserServiceNotFoundException;
import cm.klg.service_provider.domain.service_type.ServiceTypeNotFoundException;
import cm.klg.service_provider.domain.user.UserNotFoundException;
import cm.klg.service_provider.domain.user.UserPhoneNumberAlreadyExistsException;
import java.util.Optional;

public record DefaultDomainToHttpExceptionTranslator() implements DomainToHttpExceptionTranslator {
  @Override
  public HttpErrorException translate(RuntimeException ex) {
    var message = Optional.ofNullable(ex.getMessage()).orElse("missing error code");
    return switch (ex) {
      case UserNotFoundException _,
          ServiceProviderNotFoundException _,
          PortfolioItemNotFoundException _,
          ServiceTypeNotFoundException _,
          UserServiceNotFoundException _ ->
          new ResourceNotFoundException(message);
      case ServiceProviderAlreadyExistsException _,
          ServiceProviderWithPhoneNumberAlreadyExistsException _,
          ServiceProviderAlreadyProvidesServiceException _,
          UserPhoneNumberAlreadyExistsException _,
          InvalidServiceProviderStatusException _ ->
          new ConflictException(message);
      case InvalidServiceProviderPaginationDataException _,
          InvalidServiceProviderPhoneNumberException _,
          InvalidServiceProviderYearOfExperienceException _,
          CannotFavoriteOwnProfileException _,
          MissingIdentityDocumentsException _ ->
          new BadRequestException(message);
      default -> new InternalException(message, ex);
    };
  }
}
