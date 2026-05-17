package cm.klg.service_provider.adapter.rest.inbound;

import cm.klg.common.base.exception.ConflictException;
import cm.klg.common.base.exception.HttpErrorException;
import cm.klg.common.base.exception.InternalException;
import cm.klg.common.base.exception.ResourceNotFoundException;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_provider.domain.service_provider.ServiceProviderAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderWithPhoneNumberAlreadyExistsException;
import cm.klg.service_provider.domain.user.UserNotFoundException;
import java.util.Optional;

public record DefaultDomainToHttpExceptionTranslator() implements DomainToHttpExceptionTranslator {
  @Override
  public HttpErrorException translate(RuntimeException ex) {
    var message = Optional.ofNullable(ex.getMessage()).orElse("missing error code");
    return switch (ex) {
      case UserNotFoundException _, ServiceProviderNotFoundException _ ->
          new ResourceNotFoundException(message);
      case ServiceProviderAlreadyExistsException _,
          ServiceProviderWithPhoneNumberAlreadyExistsException _ ->
          new ConflictException(message);
      default -> new InternalException(message, ex);
    };
  }
}
