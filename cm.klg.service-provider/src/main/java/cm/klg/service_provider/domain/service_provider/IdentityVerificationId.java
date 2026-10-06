package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record IdentityVerificationId(UUID value) {
  public static IdentityVerificationId generate() {
    return new IdentityVerificationId(UUID.randomUUID());
  }

  public static IdentityVerificationId from(UUID value) {
    return new IdentityVerificationId(value);
  }
}
