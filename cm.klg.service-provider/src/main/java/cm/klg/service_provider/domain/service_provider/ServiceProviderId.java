package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record ServiceProviderId(UUID value) {
  public static ServiceProviderId generate() {
    return new ServiceProviderId(UUID.randomUUID());
  }
  public static ServiceProviderId from(UUID value) {return new ServiceProviderId(value);}
}
