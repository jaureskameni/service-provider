package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_400_005;

import cm.klg.common.base.exception.DomainException;

public class MissingIdentityDocumentsException extends DomainException {
  public MissingIdentityDocumentsException() {
    super(SERVICE_PROVIDER_400_005);
  }
}
