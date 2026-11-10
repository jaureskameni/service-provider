package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeSpringRepository
    extends JpaRepository<@NonNull ServiceTypeJpa, @NonNull UUID> {}
