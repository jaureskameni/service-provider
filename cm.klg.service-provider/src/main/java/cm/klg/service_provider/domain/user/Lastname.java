package cm.klg.service_provider.domain.user;

import java.util.Objects;

public record Lastname(String value) {
  public static Lastname from(String value) {
    String normalizedValue = Objects.requireNonNull(value, "lastname cannot be null").trim();
    if (normalizedValue.isEmpty()) {
      throw new IllegalArgumentException("Lastname cannot be blank");
    }
    return new Lastname(normalizedValue);
  }
}
