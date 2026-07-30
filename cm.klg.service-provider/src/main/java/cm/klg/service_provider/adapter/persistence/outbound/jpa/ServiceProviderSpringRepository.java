package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceProviderSpringRepository extends JpaRepository<ServiceProviderJpa, UUID> {
  boolean existsByUserId(UUID userId);

  boolean existsByPhoneNumber(PhoneNumberJpa phoneNumberJpa);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " LEFT JOIN FETCH s.portfolioItems WHERE s.id = :serviceProviderId")
  Optional<ServiceProviderJpa> findAggregateById(UUID serviceProviderId);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " LEFT JOIN FETCH s.portfolioItems WHERE s.id = :serviceProviderId"
          + " AND s.status = :status")
  Optional<ServiceProviderJpa> findAggregateByIdAndStatus(UUID serviceProviderId, String status);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " LEFT JOIN FETCH s.portfolioItems WHERE s.userId = :userId")
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
          """
          SELECT s.id
          FROM ServiceProviderJpa s
          WHERE s.city = :cityId
            AND s.status = :status
            AND EXISTS (
                SELECT 1
                FROM UserServiceJpa us
                WHERE us.serviceProvider = s
                  AND us.id.serviceTypeId = :serviceTypeId
            )
          ORDER BY
            CASE
              WHEN :quarterId IS NOT NULL AND s.quarter = :quarterId THEN 1
              WHEN :districtId IS NOT NULL AND s.district = :districtId THEN 2
              ELSE 3
            END,
            s.createdAt DESC
          """,
      countQuery =
          """
          SELECT COUNT(s)
          FROM ServiceProviderJpa s
          WHERE s.city = :cityId
            AND s.status = :status
            AND EXISTS (
                SELECT 1
                FROM UserServiceJpa us
                WHERE us.serviceProvider = s
                  AND us.id.serviceTypeId = :serviceTypeId
            )
          """)
  Page<UUID> searchIdsByLocationAndStatus(
      @Param("serviceTypeId") UUID serviceTypeId,
      @Param("cityId") UUID cityId,
      @Param("districtId") UUID districtId,
      @Param("quarterId") UUID quarterId,
      @Param("status") String status,
      Pageable pageable);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " LEFT JOIN FETCH s.portfolioItems WHERE s.id IN :ids")
  List<ServiceProviderJpa> findAllAggregatesByIdIn(List<UUID> ids);

  @Query(
"""
    SELECT pi
    FROM PortfolioItemJpa pi
    JOIN pi.serviceProvider sp
    WHERE sp.userId = :userId
""")
  List<PortfolioItemJpa> findPortfolioItemsByUserId(@Param("userId") UUID userId);

  @Query(
"""
    SELECT pi
    FROM PortfolioItemJpa pi
    JOIN pi.serviceProvider sp
    WHERE sp.id = :providerId
""")
  List<PortfolioItemJpa> findPortfolioItemsByProviderId(@Param("providerId") UUID providerId);
}
