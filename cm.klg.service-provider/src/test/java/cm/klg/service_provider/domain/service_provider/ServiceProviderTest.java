package cm.klg.service_provider.domain.service_provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ServiceProviderTest {

  @Test
  void rejectAndResubmit_shouldResetIdentityVerificationAndReuseAggregate() {
    var provider = newPendingProvider();
    var providerId = provider.getId();
    var adminId = new UserId(UUID.randomUUID());
    provider.reject(adminId, RejectionReason.CNI_INVALID);

    var nextFront = CniRectoMediaId.from(UUID.randomUUID());
    var nextBack = CniVersoMediaId.from(UUID.randomUUID());
    var nextPhoto = ProfileImageMediaId.from(UUID.randomUUID());
    provider.resubmit(
        provider.getLocation(),
        provider.getPhoneNumber(),
        null,
        new IdentityDocuments(nextFront, nextBack),
        nextPhoto);

    assertThat(provider.getId()).isEqualTo(providerId);
    assertThat(provider.getStatus()).isEqualTo(ServiceProviderStatus.PENDING);
    assertThat(provider.getIdentityVerification().getStatus())
        .isEqualTo(IdentityVerificationStatus.PENDING);
    assertThat(provider.getIdentityVerification().getCniRectoId()).isEqualTo(nextFront);
    assertThat(provider.getIdentityVerification().getCniVersoId()).isEqualTo(nextBack);
    assertThat(provider.getIdentityVerification().getRejectionReason()).isNull();
  }

  @Test
  void approve_shouldApproveIdentityVerificationAndRecordAdmin() {
    var provider = newPendingProvider();
    var adminId = new UserId(UUID.randomUUID());

    provider.approve(adminId);

    assertThat(provider.getIdentityVerification().getStatus())
        .isEqualTo(IdentityVerificationStatus.APPROVED);
    assertThat(provider.getIdentityVerification().getVerifiedBy()).isEqualTo(adminId);
    assertThat(provider.getIdentityVerification().getVerifiedAt()).isNotNull();
  }

  @Test
  void approvedPhotoReview_shouldKeepOldPhotoUntilApprovedAndKeepItOnRejection() {
    var provider = newPendingProvider();
    provider.approve(new UserId(UUID.randomUUID()));
    var current = provider.getProfileImageId();
    var replacement = ProfileImageMediaId.from(UUID.randomUUID());

    provider.submitProfileImageChange(replacement);
    assertThat(provider.getProfileImageId()).isEqualTo(current);
    assertThat(provider.getPendingProfileImageId()).isEqualTo(replacement);
    assertThat(provider.getProfileImageReviewStatus())
        .isEqualTo(ProfileImageReviewStatus.PENDING_REVIEW);
    provider.approveProfileImage();
    assertThat(provider.getProfileImageId()).isEqualTo(replacement);
    assertThat(provider.getPendingProfileImageId()).isNull();

    var rejectedPhoto = ProfileImageMediaId.from(UUID.randomUUID());
    provider.submitProfileImageChange(rejectedPhoto);
    provider.rejectProfileImage(RejectionReason.FACE_UNCLEAR);
    assertThat(provider.getProfileImageId()).isEqualTo(replacement);
    assertThat(provider.getPendingProfileImageId()).isNull();
    assertThat(provider.getProfileImageReviewStatus()).isEqualTo(ProfileImageReviewStatus.REJECTED);
  }

  private ServiceProvider newPendingProvider() {
    return ServiceProvider.of(
        new UserId(UUID.randomUUID()),
        new ProviderLocation(
            new UserCityId(UUID.randomUUID()),
            new UserDistrictId(UUID.randomUUID()),
            new UserQuarterId(UUID.randomUUID())),
        new PhoneNumber("+237", "678901234"),
        null,
        IdentityDocuments.of(
            CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
        ProfileImageMediaId.from(UUID.randomUUID()),
        new ArrayList<>());
  }

  @Test
  void approve_shouldSetStatusToApproved_whenStatusIsPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            IdentityDocuments.of(
                CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
            ProfileImageMediaId.from(UUID.randomUUID()),
            new ArrayList<>());

    // When
    serviceProvider.approve(adminId);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.APPROVED);
    assertThat(serviceProvider.getApprovedBy()).isEqualTo(adminId);
    assertThat(serviceProvider.getUpdatedAt()).isNotNull();
  }

  @Test
  void reject_shouldSetStatusToRejected_whenStatusIsPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    RejectionReason reason = RejectionReason.CNI_INVALID;
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            IdentityDocuments.of(
                CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
            ProfileImageMediaId.from(UUID.randomUUID()),
            new ArrayList<>());

    // When
    serviceProvider.reject(adminId, reason);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.REJECTED);
    assertThat(serviceProvider.getRejectedBy()).isEqualTo(adminId);
    assertThat(serviceProvider.getIdentityVerification().getRejectionReason()).isEqualTo(reason);
    assertThat(serviceProvider.getUpdatedAt()).isNotNull();
  }

  @Test
  void approve_shouldThrow_whenStatusIsNotPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            ServiceProviderId.generate(),
            new UserId(UUID.randomUUID()),
            ServiceProviderState.from(
                new ProviderProfile(
                    new ProviderContact(
                        new ProviderLocation(
                            new UserCityId(UUID.randomUUID()),
                            new UserDistrictId(UUID.randomUUID()),
                            new UserQuarterId(UUID.randomUUID())),
                        new PhoneNumber("+237", "678901234")),
                    null),
                new ProviderReview(
                    ServiceProviderStatus.REJECTED, null, new UserId(UUID.randomUUID()), null),
                new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
                IdentityDocuments.of(
                    CniRectoMediaId.from(UUID.randomUUID()),
                    CniVersoMediaId.from(UUID.randomUUID())),
                ProviderImages.from(
                    ProfileImageMediaId.from(UUID.randomUUID()), ProfileImageReview.none()),
                new ServiceCollections(new ArrayList<>(), new ArrayList<>())));

    assertThatThrownBy(() -> serviceProvider.approve(adminId))
        .isInstanceOf(InvalidServiceProviderStatusException.class);
  }

  @Test
  void getUserServices_shouldReturnUnmodifiableList() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            IdentityDocuments.of(
                CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
            ProfileImageMediaId.from(UUID.randomUUID()),
            new ArrayList<>());

    var userServices = serviceProvider.getUserServices();
    var dummyService =
        UserService.of(
            serviceProvider.getId(),
            new cm.klg.service_provider.domain.service_type.ServiceTypeId(UUID.randomUUID()),
            new YearOfExperience(2),
            new UserDocument(UUID.randomUUID()));

    // When & Then
    assertThatThrownBy(() -> userServices.add(dummyService))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void updatePortfolioItem_shouldThrow_whenPortfolioItemNotFound() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            IdentityDocuments.of(
                CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
            ProfileImageMediaId.from(UUID.randomUUID()),
            new ArrayList<>());

    var nonExistentId = new PortfolioItemId(UUID.randomUUID());
    PortfolioItemTitle title = PortfolioItemTitle.from("Title");
    PortfolioItemDescription description = PortfolioItemDescription.from("Description");
    PortfolioItemMediaId mediaId = new PortfolioItemMediaId(UUID.randomUUID());

    // When & Then
    serviceProvider.approve(new UserId(UUID.randomUUID()));

    assertThatThrownBy(
            () -> serviceProvider.updatePortfolioItem(nonExistentId, title, description, mediaId))
        .isInstanceOf(PortfolioItemNotFoundException.class);
  }

  @Test
  void deletePortfolioItem_shouldRemoveItem_whenItemExists() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            IdentityDocuments.of(
                CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
            ProfileImageMediaId.from(UUID.randomUUID()),
            new ArrayList<>());
    serviceProvider.approve(new UserId(UUID.randomUUID()));

    serviceProvider.addPortfolioItem(
        PortfolioItemTitle.from("Title"),
        PortfolioItemDescription.from("Description"),
        new PortfolioItemMediaId(UUID.randomUUID()));

    PortfolioItemId itemIdToDelete = serviceProvider.getPortfolioItems().get(0).getId();

    // When
    serviceProvider.deletePortfolioItem(itemIdToDelete);

    // Then
    assertThat(serviceProvider.getPortfolioItems()).isEmpty();
  }

  @Test
  void deletePortfolioItem_shouldThrow_whenPortfolioItemNotFound() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            IdentityDocuments.of(
                CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
            ProfileImageMediaId.from(UUID.randomUUID()),
            new ArrayList<>());

    var nonExistentId = new PortfolioItemId(UUID.randomUUID());
    serviceProvider.approve(new UserId(UUID.randomUUID()));

    // When & Then
    assertThatThrownBy(() -> serviceProvider.deletePortfolioItem(nonExistentId))
        .isInstanceOf(PortfolioItemNotFoundException.class);
  }

  @Test
  void deletePortfolioItem_shouldRemoveOnlySpecificItem_whenMultipleItemsExist() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            IdentityDocuments.of(
                CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
            ProfileImageMediaId.from(UUID.randomUUID()),
            new ArrayList<>());
    serviceProvider.approve(new UserId(UUID.randomUUID()));

    serviceProvider.addPortfolioItem(
        PortfolioItemTitle.from("Title 1"),
        PortfolioItemDescription.from("Description 1"),
        new PortfolioItemMediaId(UUID.randomUUID()));
    serviceProvider.addPortfolioItem(
        PortfolioItemTitle.from("Title 2"),
        PortfolioItemDescription.from("Description 2"),
        new PortfolioItemMediaId(UUID.randomUUID()));

    PortfolioItemId firstItemId = serviceProvider.getPortfolioItems().getFirst().getId();

    // When
    serviceProvider.deletePortfolioItem(firstItemId);

    // Then
    assertThat(serviceProvider.getPortfolioItems()).hasSize(1);
    assertThat(serviceProvider.getPortfolioItems().get(0).getId()).isNotEqualTo(firstItemId);
  }

  @Test
  void getPortfolioItems_shouldReturnUnmodifiableList() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            IdentityDocuments.of(
                CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
            ProfileImageMediaId.from(UUID.randomUUID()),
            new ArrayList<>());

    var portfolioItems = serviceProvider.getPortfolioItems();

    var portfolioItem =
        PortfolioItem.of(
            PortfolioItemTitle.from("Title"),
            PortfolioItemDescription.from("Description"),
            new PortfolioItemMediaId(UUID.randomUUID()));

    // When & Then
    assertThatThrownBy(() -> portfolioItems.add(portfolioItem))
        .isInstanceOf(UnsupportedOperationException.class);
  }
}
