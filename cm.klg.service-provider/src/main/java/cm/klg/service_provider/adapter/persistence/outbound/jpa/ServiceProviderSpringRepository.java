package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceProviderSpringRepository
    extends JpaRepository<@NonNull ServiceProviderJpa, @NonNull UUID> {
  boolean existsByUserId(UUID userId);

  @Query("SELECT s.status FROM ServiceProviderJpa s WHERE s.userId = :userId")
  Optional<String> findStatusByUserId(@Param("userId") UUID userId);

  boolean existsByPhoneNumber(PhoneNumberJpa phoneNumberJpa);

  boolean existsByPhoneNumberAndUserIdNot(PhoneNumberJpa phoneNumberJpa, UUID userId);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " LEFT JOIN FETCH s.identityVerification"
          + " WHERE s.id = :serviceProviderId")
  Optional<@NonNull ServiceProviderJpa> findAggregateById(
      @Param("serviceProviderId") UUID serviceProviderId);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.portfolioItems"
          + " LEFT JOIN FETCH s.identityVerification"
          + " WHERE s.id = :serviceProviderId")
  Optional<@NonNull ServiceProviderJpa> findAggregateWithPortfolioById(
      @Param("serviceProviderId") UUID serviceProviderId);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.userServices"
          + " WHERE s.userId = :userId")
  Optional<@NonNull ServiceProviderJpa> findAggregateByUserId(@Param("userId") UUID userId);

  @Query(
      "SELECT DISTINCT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.portfolioItems"
          + " WHERE s.userId = :userId")
  Optional<@NonNull ServiceProviderJpa> findAggregateWithPortfolioByUserId(
      @Param("userId") UUID userId);

  @Query(
      value = "SELECT s FROM ServiceProviderJpa s ORDER BY s.createdAt DESC",
      countQuery = "SELECT COUNT(s) FROM ServiceProviderJpa s")
  Page<@NonNull ServiceProviderJpa> findAllAsView(Pageable pageable);

  @Query(
      value =
          "SELECT s FROM ServiceProviderJpa s WHERE"
              + " s.status = :status ORDER BY s.createdAt DESC",
      countQuery = "SELECT COUNT(s) FROM ServiceProviderJpa s WHERE s.status = :status")
  Page<@NonNull ServiceProviderJpa> findAllByStatus(
      @Param("status") String status, Pageable pageable);

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
  Page<@NonNull ServiceProviderJpa> searchIdsByLocationAndStatus(
      @Param("serviceTypeId") UUID serviceTypeId,
      @Param("cityId") UUID cityId,
      @Param("districtId") UUID districtId,
      @Param("quarterId") UUID quarterId,
      @Param("status") String status,
      Pageable pageable);

  @Query(
"""
    SELECT pi
    FROM PortfolioItemJpa pi
    JOIN pi.serviceProvider sp
    WHERE sp.userId = :userId
""")
  List<PortfolioItemJpa> findPortfolioItemsByUserId(@Param("userId") UUID userId);

  @Query(
      "SELECT s FROM ServiceProviderJpa s WHERE s.id = :serviceProviderId AND s.status = :status")
  Optional<@NonNull ServiceProviderJpa> findByIdAndStatus(
      @Param("serviceProviderId") UUID serviceProviderId, @Param("status") String status);

  @Query(
      "SELECT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.identityVerification"
          + " WHERE s.id = :serviceProviderId")
  Optional<@NonNull ServiceProviderJpa> findForVerificationById(
      @Param("serviceProviderId") UUID serviceProviderId);

  @Query(
      "SELECT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.identityVerification"
          + " WHERE s.userId = :userId")
  Optional<@NonNull ServiceProviderJpa> findIdentityVerificationByUserId(
      @Param("userId") UUID userId);

  @Query(
      "SELECT s FROM ServiceProviderJpa s LEFT JOIN FETCH s.identityVerification"
          + " WHERE s.id = :serviceProviderId")
  Optional<@NonNull ServiceProviderJpa> findForProfileImageReviewById(
      @Param("serviceProviderId") UUID serviceProviderId);

  Optional<@NonNull ServiceProviderJpa> findByUserId(UUID userId);

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

  boolean existsByIdAndStatus(UUID id, String status);

  @Query(
      value =
          "SELECT new cm.klg.service_provider.adapter.persistence.outbound.jpa."
              + "ProfileImageReviewView(s.id, s.userId, u.id, u.firstname, u.lastname,"
              + " u.emailAddress, u.createdAt, s.phoneNumber,"
              + " s.city, s.district, s.quarter, s.status,"
              + " s.profileImageId, s.pendingProfileImageId, s.profileImageReviewStatus,"
              + " s.updatedAt)"
              + " FROM ServiceProviderJpa s JOIN UserJpa u ON u.id = s.userId"
              + " WHERE s.profileImageReviewStatus = :status"
              + " ORDER BY s.updatedAt DESC",
      countQuery =
          "SELECT COUNT(s) FROM ServiceProviderJpa s JOIN UserJpa u ON u.id = s.userId"
              + " WHERE s.profileImageReviewStatus = :status")
  Page<@NonNull ProfileImageReviewView> findProfileImageReviewQueue(
      @Param("status") String status, Pageable pageable);
}
