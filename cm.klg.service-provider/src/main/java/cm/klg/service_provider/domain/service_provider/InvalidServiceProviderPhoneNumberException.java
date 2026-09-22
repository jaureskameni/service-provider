package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_400_003;

import cm.klg.common.base.exception.DomainException;

public class InvalidServiceProviderPhoneNumberException extends DomainException {
  public InvalidServiceProviderPhoneNumberException() {
    super(SERVICE_PROVIDER_400_003);
  }
}
