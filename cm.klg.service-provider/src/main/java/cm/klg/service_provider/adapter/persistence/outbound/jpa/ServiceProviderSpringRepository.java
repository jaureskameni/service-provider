package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceProviderSpringRepository extends JpaRepository<ServiceProviderJpa, UUID> {
  boolean existsByUserId(UUID userId);

  boolean existsByPhoneNumber(PhoneNumberJpa phoneNumberJpa);

  @Query(
      "SELECT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices WHERE s.id ="
          + " :serviceProviderId")
  Optional<ServiceProviderJpa> findAggregateById(UUID serviceProviderId);
}
