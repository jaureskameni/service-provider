package cm.klg.service_provider.domain.user;

import org.jspecify.annotations.Nullable;

public record Firstname(String value) {
  public static @Nullable Firstname from(@Nullable String value) {
    if (value == null) {
      return null;
    }
    if (value.isBlank()) {
      throw new IllegalArgumentException("Firstname cannot be blank");
    }
    return new Firstname(value.trim());
  }
}
