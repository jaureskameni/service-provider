package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record UserCityId(UUID value) {
  public static UserCityId from(UUID value) {
    return new UserCityId(value);
  }
}
