package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderSpringRepository extends JpaRepository<ServiceProviderJpa, UUID> {
  boolean existsByUserId(UUID userId);

  boolean existsByPhoneNumber(PhoneNumberJpa phoneNumberJpa);
}
