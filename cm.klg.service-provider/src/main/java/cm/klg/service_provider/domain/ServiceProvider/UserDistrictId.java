package cm.klg.service_provider.domain.ServiceProvider;

import java.util.UUID;

public record UserDistrictId(UUID value) {
  public static UserDistrictId from(UUID value) {
    return new UserDistrictId(value);
  }
}
