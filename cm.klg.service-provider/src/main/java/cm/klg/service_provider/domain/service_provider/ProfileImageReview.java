package cm.klg.service_provider.domain.service_provider;

import org.jspecify.annotations.Nullable;

public record ProfileImageReview(
    ProfileImageReviewStatus status,
    @Nullable ProfileImageMediaId pendingMediaId,
    @Nullable RejectionReason rejectionReason) {

  public static ProfileImageReview none() {
    return new ProfileImageReview(ProfileImageReviewStatus.NONE, null, null);
  }

  public static ProfileImageReview pending(ProfileImageMediaId pendingMediaId) {
    return new ProfileImageReview(ProfileImageReviewStatus.PENDING_REVIEW, pendingMediaId, null);
  }

  public static ProfileImageReview rejected(RejectionReason rejectionReason) {
    return new ProfileImageReview(ProfileImageReviewStatus.REJECTED, null, rejectionReason);
  }
}
