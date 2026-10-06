package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_404_005;

import cm.klg.common.base.exception.DomainException;

public class PortfolioItemNotFoundException extends DomainException {
  public PortfolioItemNotFoundException() {
    super(SERVICE_PROVIDER_404_005);
  }
}
