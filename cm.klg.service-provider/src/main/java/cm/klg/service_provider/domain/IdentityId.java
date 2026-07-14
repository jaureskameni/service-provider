package cm.klg.service_provider.domain;

import java.util.UUID;

public record IdentityId(UUID value) {
  public static IdentityId from(UUID value) {
    return new IdentityId(value);
  }

  public static IdentityId from(UserId userId) {
    return new IdentityId(userId.value());
  }
}
