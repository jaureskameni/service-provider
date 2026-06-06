package cm.klg.service_provider.domain.provider_client;

import java.util.UUID;

public record ProviderClientId(UUID value) {
  public static ProviderClientId generate() {
    return new ProviderClientId(UUID.randomUUID());
  }
}
