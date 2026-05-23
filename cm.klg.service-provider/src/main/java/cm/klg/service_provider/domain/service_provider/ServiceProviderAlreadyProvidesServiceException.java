package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_409_003;

import cm.klg.common.base.exception.DomainException;

public class ServiceProviderAlreadyProvidesServiceException extends DomainException {
  public ServiceProviderAlreadyProvidesServiceException() {
    super(SERVICE_PROVIDER_409_003);
  }
}
