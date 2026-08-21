package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProviderClientSpringRepository extends JpaRepository<ProviderClientJpa, UUID> {
  boolean existsByUserIdAndProviderId(UUID userId, UUID providerId);

  @Modifying
  @Query(
      value =
          "INSERT INTO t_provider_client (c_id, c_user_id, c_provider_id, c_created_at)"
              + " VALUES (:id, :userId, :providerId, :createdAt)"
              + " ON CONFLICT (c_user_id, c_provider_id) DO NOTHING",
      nativeQuery = true)
  int insertIfAbsent(
      @Param("id") String id,
      @Param("userId") String userId,
      @Param("providerId") String providerId,
      @Param("createdAt") LocalDateTime createdAt);
}
