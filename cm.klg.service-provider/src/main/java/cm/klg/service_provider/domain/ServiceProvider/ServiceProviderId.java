package cm.klg.service_provider.domain.ServiceProvider;

import java.util.UUID;

public record ServiceProviderId(UUID value) {
  public static ServiceProviderId generate() {
    return new ServiceProviderId(UUID.randomUUID());
  }
}
