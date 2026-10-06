package cm.klg.service_provider.domain.service_provider;

import org.jspecify.annotations.Nullable;

public record ProviderImages(
    @Nullable ProfileImageMediaId current,
    @Nullable ProfileImageMediaId pending,
    ProfileImageReviewStatus reviewStatus,
    @Nullable RejectionReason rejectionReason) {

  public static ProviderImages from(
      @Nullable ProfileImageMediaId current, ProfileImageReview review) {
    return new ProviderImages(
        current, review.pendingMediaId(), review.status(), review.rejectionReason());
  }
}
