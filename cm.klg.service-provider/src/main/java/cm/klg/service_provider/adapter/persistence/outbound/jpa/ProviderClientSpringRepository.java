package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderClientSpringRepository extends JpaRepository<ProviderClientJpa, UUID> {
  boolean existsByUserIdAndProviderId(UUID userId, UUID providerId);
}
