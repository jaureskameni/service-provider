package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_404_004;

import cm.klg.common.base.exception.DomainException;

public class UserServiceNotFoundException extends DomainException {
  public UserServiceNotFoundException() {
    super(SERVICE_PROVIDER_404_004);
  }
}
