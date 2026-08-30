package cm.klg.service_provider.domain.favorite;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class FavoriteProvider {
  private final FavoriteProviderId id;
  private final UserId userId;
  private final ServiceProviderId providerId;
  private final CreatedAt createdAt;

  public FavoriteProvider(
      FavoriteProviderId id, UserId userId, ServiceProviderId providerId, CreatedAt createdAt) {
    this.id = id;
    this.userId = userId;
    this.providerId = providerId;
    this.createdAt = createdAt;
  }

  public static FavoriteProvider of(UserId userId, ServiceProviderId providerId) {
    return new FavoriteProvider(
        FavoriteProviderId.generate(), userId, providerId, new CreatedAt(LocalDateTime.now()));
  }
}
