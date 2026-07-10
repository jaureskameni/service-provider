package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSpringRepository extends JpaRepository<UserJpa, UUID> {
  Optional<UserJpa> findByIdentityId(UUID identityId);

  boolean existsByIdentityId(UUID identityId);

  List<UserJpa> findAllByIdentityIdIn(List<UUID> uuids);
}
