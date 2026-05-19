package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceProviderSpringRepository extends JpaRepository<ServiceProviderJpa, UUID> {
  boolean existsByUserId(UUID userId);

  boolean existsByPhoneNumber(PhoneNumberJpa phoneNumberJpa);

  @Query(
      "SELECT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices WHERE s.id ="
          + " :serviceProviderId")
  Optional<ServiceProviderJpa> findAggregateById(UUID serviceProviderId);

  @Query(
      "SELECT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices WHERE s.userId ="
          + " :userId")
  Optional<ServiceProviderJpa> findAggregateByUserId(UUID userId);

  @Query(
      value =
          "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices ORDER BY"
              + " s.createdAt DESC",
      countQuery = "SELECT COUNT(DISTINCT s) FROM ServiceProviderJpa s")
  Page<ServiceProviderJpa> findAllAggregate(Pageable pageable);

  @Query(
      value =
          "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices WHERE"
              + " s.status = :status ORDER BY s.createdAt DESC",
      countQuery = "SELECT COUNT(DISTINCT s) FROM ServiceProviderJpa s WHERE s.status = :status")
  Page<ServiceProviderJpa> findAllAggregateByStatus(String status, Pageable pageable);
}
