package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSpringRepository extends JpaRepository<UserJpa, UUID> {
  @Query("SELECT u FROM UserJpa u WHERE u.identityId = :identityId")
  Optional<UserJpa> findByIdentityId(@Param("identityId") UUID identityId);

  boolean existsByIdentityId(UUID identityId);

  List<UserJpa> findAllByIdentityIdIn(List<UUID> uuids);
}
