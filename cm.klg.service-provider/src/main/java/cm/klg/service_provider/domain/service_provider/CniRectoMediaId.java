package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record CniRectoMediaId(UUID value) {
  public CniRectoMediaId {
    java.util.Objects.requireNonNull(value);
  }

  public static CniRectoMediaId from(UUID value) {
    return new CniRectoMediaId(value);
  }
}
