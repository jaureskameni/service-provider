package cm.klg.service_provider.domain.service_provider;

import org.jspecify.annotations.Nullable;

public record ProviderLocation(
    UserCityId cityId, UserDistrictId districtId, @Nullable UserQuarterId quarterId) {

  public static ProviderLocation of(
      UserCityId cityId, UserDistrictId districtId, @Nullable UserQuarterId quarterId) {
    return new ProviderLocation(cityId, districtId, quarterId);
  }
}
