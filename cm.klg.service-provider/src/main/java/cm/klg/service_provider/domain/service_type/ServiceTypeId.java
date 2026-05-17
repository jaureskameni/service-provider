package cm.klg.service_provider.domain.service_type;

import java.util.UUID;

public record ServiceTypeId(UUID value) {
  public static ServiceTypeId generate() {
    return new ServiceTypeId(UUID.randomUUID());
  }
}
