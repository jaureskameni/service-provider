package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record UserQuarterId(UUID value) {
  public static UserQuarterId from(UUID value) {
    return new UserQuarterId(value);
  }
}
