package cm.klg.service_provider.domain;

import java.util.UUID;

public record IdentityId(UUID value) {
  public static IdentityId from(UUID value) {
    return new IdentityId(value);
  }
}
