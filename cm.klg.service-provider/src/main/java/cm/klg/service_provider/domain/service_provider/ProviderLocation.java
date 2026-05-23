package cm.klg.service_provider.domain.service_provider;

import java.util.Objects;

public record ProviderLocation(
    UserCityId cityId, UserDistrictId districtId, UserQuarterId quarterId) {

  public ProviderLocation {
    Objects.requireNonNull(cityId, "cityId must not be null");
    Objects.requireNonNull(districtId, "districtId must not be null");
    Objects.requireNonNull(quarterId, "quarterId must not be null");
  }

  public static ProviderLocation of(
      UserCityId cityId, UserDistrictId districtId, UserQuarterId quarterId) {
    return new ProviderLocation(cityId, districtId, quarterId);
  }
}
