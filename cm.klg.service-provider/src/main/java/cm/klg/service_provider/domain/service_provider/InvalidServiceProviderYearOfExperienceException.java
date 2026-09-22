package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.exception.ServiceProviderErrorCode.SERVICE_PROVIDER_400_004;

import cm.klg.common.base.exception.DomainException;

public class InvalidServiceProviderYearOfExperienceException extends DomainException {
  public InvalidServiceProviderYearOfExperienceException() {
    super(SERVICE_PROVIDER_400_004);
  }
}
