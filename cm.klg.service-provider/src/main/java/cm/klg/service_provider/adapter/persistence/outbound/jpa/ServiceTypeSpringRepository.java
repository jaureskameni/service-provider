package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeSpringRepository extends JpaRepository<ServiceTypeJpa, UUID> {}
