package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record CniVersoMediaId(UUID value) {
  public CniVersoMediaId {
    java.util.Objects.requireNonNull(value);
  }

  public static CniVersoMediaId from(UUID value) {
    return new CniVersoMediaId(value);
  }
}
