package cm.klg.service_provider.domain.favorite;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_400_002;

import cm.klg.common.base.exception.DomainException;

public class CannotFavoriteOwnProfileException extends DomainException {
  public CannotFavoriteOwnProfileException() {
    super(SERVICE_PROVIDER_400_002);
  }
}
