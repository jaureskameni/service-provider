package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.favorite.FavoriteProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Slf4j
public class FavoriteProviderJpaRepository implements FavoriteProviderRepository {
  private final FavoriteProviderSpringRepository favoriteProviderSpringRepository;
  private final UserSpringRepository userSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public boolean existsByUserIdAndProviderId(UserId userId, ServiceProviderId providerId) {
    return favoriteProviderSpringRepository.existsByUserIdAndProviderId(
        userId.value(), providerId.value());
  }

  @Override
  public void insertIfAbsent(FavoriteProvider favoriteProvider) {
    FavoriteProviderJpa favoriteProviderJpa = jpaMapper.toJpa(favoriteProvider);
    int insertedRows =
        favoriteProviderSpringRepository.insertIfAbsent(
            favoriteProviderJpa.getId().toString(),
            favoriteProviderJpa.getUserId().toString(),
            favoriteProviderJpa.getProviderId().toString(),
            favoriteProviderJpa.getCreatedAt());
    if (insertedRows == 0) {
      log.debug(
          "Favorite relation for user {} and provider {} already exists, skipping insertion.",
          favoriteProvider.getUserId().value(),
          favoriteProvider.getProviderId().value());
    }
  }

  @Override
  public void deleteByUserIdAndProviderId(UserId userId, ServiceProviderId providerId) {
    favoriteProviderSpringRepository.deleteByUserIdAndProviderId(
        userId.value(), providerId.value());
  }

  @Override
  public PageData<ServiceProviderView1> findFavoritesByUserId(
      UserId userId, PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    Page<ServiceProviderJpa> page =
        favoriteProviderSpringRepository.findFavoriteProvidersByUserId(userId.value(), pageable);
    return toPageData(page);
  }

  private PageData<ServiceProviderView1> toPageData(Page<ServiceProviderJpa> serviceProviderJpas) {
    if (serviceProviderJpas.isEmpty()) {
      return new PageData<>(serviceProviderJpas.getTotalElements(), Collections.emptyList());
    }

    List<ServiceProviderJpa> providerJpasContent = serviceProviderJpas.getContent();

    List<UUID> userIds =
        serviceProviderJpas.getContent().stream().map(ServiceProviderJpa::getUserId).toList();

    Map<UUID, UserJpa> usersById =
        userSpringRepository.findAllByIdentityIdIn(userIds).stream()
            .collect(Collectors.toMap(UserJpa::getIdentityId, Function.identity()));

    return new PageData<>(
        serviceProviderJpas.getTotalElements(),
        providerJpasContent.stream()
            .map(sp -> jpaMapper.toServiceProviderView1(sp, usersById.get(sp.getUserId())))
            .toList());
  }
}
