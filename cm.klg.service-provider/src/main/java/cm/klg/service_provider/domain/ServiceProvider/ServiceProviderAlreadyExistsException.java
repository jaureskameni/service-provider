package cm.klg.service_provider.domain.ServiceProvider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_409_001;

import cm.klg.common.base.exception.DomainException;

public class ServiceProviderAlreadyExistsException extends DomainException {
  public ServiceProviderAlreadyExistsException() {
    super(SERVICE_PROVIDER_409_001);
  }
}
