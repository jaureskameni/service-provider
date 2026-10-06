package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record ProfileImageMediaId(UUID value) {
  public ProfileImageMediaId {
    java.util.Objects.requireNonNull(value);
  }

  public static ProfileImageMediaId from(UUID value) {
    return new ProfileImageMediaId(value);
  }
}
