package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_409_004;

import cm.klg.common.base.exception.DomainException;

public class InvalidServiceProviderStatusException extends DomainException {
  public InvalidServiceProviderStatusException() {
    super(SERVICE_PROVIDER_409_004);
  }
}
