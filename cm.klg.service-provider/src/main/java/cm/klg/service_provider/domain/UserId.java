package cm.klg.service_provider.domain;

import java.util.UUID;

public record UserId(UUID value) {
  public static UserId from(UUID value) {
    return new UserId(value);
  }
}
