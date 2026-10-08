package cm.klg.service_provider.domain.service_provider;

import cm.klg.service_provider.domain.UserId;
import org.jspecify.annotations.Nullable;

public record IdentityVerificationReview(
    IdentityVerificationStatus status,
    @Nullable RejectionReason rejectionReason,
    @Nullable VerifiedAt verifiedAt,
    @Nullable UserId verifiedBy) {

  public static IdentityVerificationReview pending() {
    return new IdentityVerificationReview(IdentityVerificationStatus.PENDING, null, null, null);
  }
}
