package cm.klg.service_provider.domain.user;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_404_001;

import cm.klg.common.base.exception.DomainException;

public class UserNotFoundException extends DomainException {
  public UserNotFoundException() {
    super(SERVICE_PROVIDER_404_001);
  }
}
