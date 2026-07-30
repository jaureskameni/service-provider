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
  Optional<ServiceProviderJpa> findAggregateById(
      @Param("serviceProviderId") UUID serviceProviderId);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " LEFT JOIN FETCH s.portfolioItems WHERE s.id = :serviceProviderId"
          + " AND s.status = :status")
  Optional<ServiceProviderJpa> findAggregateByIdAndStatus(
      @Param("serviceProviderId") UUID serviceProviderId, @Param("status") String status);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " LEFT JOIN FETCH s.portfolioItems WHERE s.userId = :userId"
          + " AND s.status = :status")
  Optional<ServiceProviderJpa> findAggregateByUserId(
      @Param("userId") UUID userId, @Param("status") String status);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " LEFT JOIN FETCH s.portfolioItems WHERE s.userId = :userId")
  Optional<ServiceProviderJpa> findAggregateByUserId(@Param("userId") UUID userId);

  @Query(
      value = "SELECT s FROM ServiceProviderJpa s ORDER BY s.createdAt DESC",
      countQuery = "SELECT COUNT(s) FROM ServiceProviderJpa s")
  Page<ServiceProviderJpa> findAllAsView(Pageable pageable);

  @Query(
      value =
          "SELECT s FROM ServiceProviderJpa s WHERE"
              + " s.status = :status ORDER BY s.createdAt DESC",
      countQuery = "SELECT COUNT(s) FROM ServiceProviderJpa s WHERE s.status = :status")
  Page<ServiceProviderJpa> findAllByStatus(@Param("status") String status, Pageable pageable);

  @Query(
      value =
          """
          SELECT s
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
  Page<ServiceProviderJpa> searchIdsByLocationAndStatus(
      @Param("serviceTypeId") UUID serviceTypeId,
      @Param("cityId") UUID cityId,
      @Param("districtId") UUID districtId,
      @Param("quarterId") UUID quarterId,
      @Param("status") String status,
      Pageable pageable);

  @Query("SELECT s FROM ServiceProviderJpa s WHERE s.id IN :ids")
  List<ServiceProviderJpa> findAllAggregatesByIdIn(@Param("ids") List<UUID> ids);

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

  @Query("SELECT s FROM ServiceProviderJpa s WHERE s.id = :serviceProviderId")
  Optional<ServiceProviderJpa> findById(@Param("serviceProviderId") UUID serviceProviderId);

  @Query(
"""
    SELECT us
    FROM UserServiceJpa us
    WHERE us.id.serviceProviderId = :serviceProviderId
""")
  List<UserServiceJpa> findUserServicesByServiceProviderId(
      @Param("serviceProviderId") UUID serviceProviderId);

  @Query(
"""
    SELECT pi
    FROM PortfolioItemJpa pi
    WHERE pi.serviceProvider.id = :serviceProviderId
""")
  List<PortfolioItemJpa> findPortfolioItemsByServiceProviderId(
      @Param("serviceProviderId") UUID serviceProviderId);
}
