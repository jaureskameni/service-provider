package cm.klg.service_provider.domain.ServiceProvider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_409_002;

import cm.klg.common.base.exception.DomainException;

public class ServiceProviderWithPhoneNumberAlreadyExistsException extends DomainException {
  public ServiceProviderWithPhoneNumberAlreadyExistsException() {
    super(SERVICE_PROVIDER_409_002);
  }
}
