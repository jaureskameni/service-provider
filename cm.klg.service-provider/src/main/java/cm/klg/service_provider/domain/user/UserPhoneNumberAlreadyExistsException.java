package cm.klg.service_provider.domain.user;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_409_005;

import cm.klg.common.base.exception.DomainException;

public class UserPhoneNumberAlreadyExistsException extends DomainException {
  public UserPhoneNumberAlreadyExistsException() {
    super(SERVICE_PROVIDER_409_005);
  }
}
