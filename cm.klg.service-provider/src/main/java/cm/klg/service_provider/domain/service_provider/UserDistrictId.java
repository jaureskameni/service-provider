package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record UserDistrictId(UUID value) {
  public static UserDistrictId from(UUID value) {
    return new UserDistrictId(value);
  }
}
