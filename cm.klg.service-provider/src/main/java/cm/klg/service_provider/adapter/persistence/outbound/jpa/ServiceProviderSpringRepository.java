package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import java.util.List;
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
      "SELECT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices WHERE s.id ="
          + " :serviceProviderId AND s.status = :status")
  Optional<ServiceProviderJpa> findAggregateByIdAndStatus(UUID serviceProviderId, String status);

  @Query(
      "SELECT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices WHERE s.userId ="
          + " :userId")
  Optional<ServiceProviderJpa> findAggregateByUserId(UUID userId);

  @Query(
      value = "SELECT s.id FROM ServiceProviderJpa s ORDER BY" + " s.createdAt DESC",
      countQuery = "SELECT COUNT(DISTINCT s) FROM ServiceProviderJpa s")
  Page<UUID> findAllIds(Pageable pageable);

  @Query(
      value =
          "SELECT s.id FROM ServiceProviderJpa s WHERE"
              + " s.status = :status ORDER BY s.createdAt DESC",
      countQuery = "SELECT COUNT(DISTINCT s) FROM ServiceProviderJpa s WHERE s.status = :status")
  Page<UUID> findAllIdsByStatus(String status, Pageable pageable);

  @Query(
      value =
          "SELECT DISTINCT s.id FROM ServiceProviderJpa s JOIN s.userServices us "
              + "WHERE s.city = :cityId "
              + "AND us.id.serviceTypeId = :serviceTypeId "
              + "AND (s.status = :status) "
              + "ORDER BY "
              + "CASE "
              + "  WHEN s.quarter = :quarterId THEN 1 "
              + "  WHEN s.district = :districtId THEN 2 "
              + "  ELSE 3 "
              + "END ASC, s.createdAt DESC",
      countQuery =
          "SELECT COUNT(DISTINCT s) FROM ServiceProviderJpa s JOIN s.userServices us WHERE"
              + " s.city = :cityId AND us.id.serviceTypeId = :serviceTypeId AND (:status IS NULL"
              + " OR s.status = :status)")
  Page<UUID> searchIdsByLocationAndStatus(
      UUID serviceTypeId,
      UUID cityId,
      UUID districtId,
      UUID quarterId,
      String status,
      Pageable pageable);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices WHERE s.id IN"
          + " :ids")
  List<ServiceProviderJpa> findAllAggregatesByIdIn(List<UUID> ids);
}
