package cm.klg.service_provider.application.views;

import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.service_provider.IdentityVerificationStatus;
import cm.klg.service_provider.domain.service_provider.ProfileImageReviewStatus;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface ServiceProviderViews {
  record ServiceProviderView1(
      UUID id,
      UUID userId,
      UserView user,
      UUID cityId,
      UUID districtId,
      @Nullable UUID quarterId,
      @Nullable UUID approvedBy,
      @Nullable UUID rejectedBy,
      @Nullable String rejectionReason,
      @Nullable String about,
      PhoneNumber phoneNumber,
      String status,
      UUID profileImageId,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {}

  record ServiceProviderView2(
      UUID id,
      UUID userId,
      UserView user,
      UUID cityId,
      UUID districtId,
      @Nullable UUID quarterId,
      @Nullable UUID approvedBy,
      @Nullable UUID rejectedBy,
      @Nullable String rejectionReason,
      @Nullable String about,
      PhoneNumber phoneNumber,
      String status,
      UUID profileImageId,
      @Nullable UUID pendingProfileImageId,
      ProfileImageReviewStatus profileImageReviewStatus,
      IdentityVerificationStatus identityVerificationStatus,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      List<UserServiceView> services,
      List<PortfolioView> portfolios,
      @Nullable IdentityVerificationView identityVerification) {}

  record ServiceProviderVerificationView(
      UUID id,
      UUID userId,
      UserView user,
      UUID cityId,
      UUID districtId,
      @Nullable UUID quarterId,
      @Nullable UUID approvedBy,
      @Nullable UUID rejectedBy,
      @Nullable String rejectionReason,
      @Nullable String about,
      PhoneNumber phoneNumber,
      String status,
      UUID profileImageId,
      @Nullable UUID pendingProfileImageId,
      ProfileImageReviewStatus profileImageReviewStatus,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      List<UserServiceView> services,
      List<PortfolioView> portfolios,
      @Nullable IdentityVerificationView identityVerification) {}

  record ServiceProviderView3(
      UUID id,
      UUID userId,
      UserView user,
      PhoneNumber phoneNumber,
      UUID cityId,
      UUID districtId,
      @Nullable UUID quarterId,
      @Nullable String about,
      ServiceProviderStatus status,
      UUID profileImageId,
      UUID pendingProfileImageId,
      ProfileImageReviewStatus profileImageReviewStatus,
      @Nullable RejectionReason profileImageRejectionReason,
      @Nullable LocalDateTime submittedAt,
      IdentityVerificationView identityVerification,
      LocalDateTime createdAt,
      @Nullable LocalDateTime updatedAt,
      List<UserServiceView> services) {}

  record ProfileImageReviewSummaryView(
      UUID serviceProviderId,
      UUID userId,
      UserView user,
      PhoneNumber phoneNumber,
      UUID cityId,
      UUID districtId,
      @Nullable UUID quarterId,
      ServiceProviderStatus status,
      UUID currentProfileImageId,
      UUID pendingProfileImageId,
      ProfileImageReviewStatus profileImageReviewStatus,
      @Nullable LocalDateTime submittedAt) {}

  record RejectedServiceProviderIdentityView(
      UUID providerId,
      UUID cniRectoId,
      UUID cniVersoId,
      UUID profileImageId,
      IdentityVerificationStatus status,
      RejectionReason rejectionReason) {}

  record ServiceProviderIdentityView(
      UUID providerId,
      @Nullable UUID profileImageId,
      IdentityVerificationView identityVerification) {}

  record IdentityVerificationView(
      UUID id,
      @Nullable UUID cniRectoId,
      @Nullable UUID cniVersoId,
      IdentityVerificationStatus status,
      @Nullable RejectionReason rejectionReason,
      @Nullable LocalDateTime verifiedAt,
      @Nullable UUID verifiedBy) {}

  record UserView(
      UUID id,
      @Nullable String firstname,
      String lastname,
      @Nullable String email,
      LocalDateTime createdAt) {}
}
