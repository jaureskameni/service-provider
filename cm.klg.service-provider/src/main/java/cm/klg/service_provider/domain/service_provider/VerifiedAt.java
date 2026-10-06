package cm.klg.service_provider.domain.service_provider;

import java.time.LocalDateTime;
import java.util.Objects;

public record VerifiedAt(LocalDateTime value) {
  public VerifiedAt {
    Objects.requireNonNull(value, "verifiedAt must not be null");
  }

  public static VerifiedAt from(LocalDateTime value) {
    return new VerifiedAt(value);
  }

  public static VerifiedAt now() {
    return new VerifiedAt(LocalDateTime.now());
  }
}
