package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_400_001;

import cm.klg.common.base.exception.DomainException;

public class InvalidServiceProviderDataException extends DomainException {
  public InvalidServiceProviderDataException() {
    super(SERVICE_PROVIDER_400_001);
  }
}
