package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.favorite.FavoriteProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;

public interface FavoriteProviderRepository {
  boolean existsByUserIdAndProviderId(UserId userId, ServiceProviderId providerId);

  void insertIfAbsent(FavoriteProvider favoriteProvider);

  void deleteByUserIdAndProviderId(UserId userId, ServiceProviderId providerId);

  PageData<ServiceProviderView1> findFavoritesByUserId(
      UserId userId, PaginationFetchRequest pagination);
}
