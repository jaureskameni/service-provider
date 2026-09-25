package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteProviderSpringRepository extends JpaRepository<FavoriteProviderJpa, UUID> {
  boolean existsByUserIdAndProviderId(UUID userId, UUID providerId);

  @Modifying
  @Query(
      value =
          "INSERT INTO t_favorite_provider (c_id, c_user_id, c_provider_id, c_created_at)"
              + " VALUES (:id, :userId, :providerId, :createdAt)"
              + " ON CONFLICT (c_user_id, c_provider_id) DO NOTHING",
      nativeQuery = true)
  int insertIfAbsent(
      @Param("id") String id,
      @Param("userId") String userId,
      @Param("providerId") String providerId,
      @Param("createdAt") LocalDateTime createdAt);

  @Modifying
  @Query(
      "DELETE FROM FavoriteProviderJpa f WHERE f.userId = :userId AND f.providerId = :providerId")
  void deleteByUserIdAndProviderId(
      @Param("userId") UUID userId, @Param("providerId") UUID providerId);

  @Query(
      value =
          """
          SELECT s
          FROM FavoriteProviderJpa f
          JOIN ServiceProviderJpa s ON s.id = f.providerId
          WHERE f.userId = :userId
          ORDER BY f.createdAt DESC, s.id DESC
          """,
      countQuery =
          """
          SELECT COUNT(f)
          FROM FavoriteProviderJpa f
          WHERE f.userId = :userId
          """)
  Page<ServiceProviderJpa> findFavoriteProvidersByUserId(
      @Param("userId") UUID userId, Pageable pageable);
}
