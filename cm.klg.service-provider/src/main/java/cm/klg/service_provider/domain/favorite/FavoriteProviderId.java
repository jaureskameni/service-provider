package cm.klg.service_provider.domain.favorite;

import java.util.UUID;

public record FavoriteProviderId(UUID value) {
  public static FavoriteProviderId generate() {
    return new FavoriteProviderId(UUID.randomUUID());
  }

  public static FavoriteProviderId from(UUID value) {
    return new FavoriteProviderId(value);
  }
}
