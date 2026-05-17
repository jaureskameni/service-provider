package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_404_002;

import cm.klg.common.base.exception.DomainException;

public class ServiceProviderNotFoundException extends DomainException {
  public ServiceProviderNotFoundException() {
    super(SERVICE_PROVIDER_404_002);
  }
}
