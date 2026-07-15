package cm.klg.service_provider.domain.service_type;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_404_003;

import cm.klg.common.base.exception.DomainException;

public class ServiceTypeNotFoundException extends DomainException {
  public ServiceTypeNotFoundException() {
    super(SERVICE_PROVIDER_404_003);
  }
}
