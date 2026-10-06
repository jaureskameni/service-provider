package cm.klg.service_provider.domain.service_provider;

import cm.klg.service_provider.domain.UserId;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class IdentityVerification {
  private final IdentityVerificationId id;
  private IdentityVerificationStatus status;
  private IdentityVerificationDocuments documents;
  @Nullable private RejectionReason rejectionReason;
  @Nullable private VerifiedAt verifiedAt;
  @Nullable private UserId verifiedBy;

  private IdentityVerification(
      IdentityVerificationId id,
      IdentityVerificationDocuments documents,
      IdentityVerificationReview review) {
    this.id = id;
    this.documents = documents;
    this.status = review.status();
    this.rejectionReason = review.rejectionReason();
    this.verifiedAt = review.verifiedAt();
    this.verifiedBy = review.verifiedBy();
  }

  public static IdentityVerification of(CniRectoMediaId cniRectoId, CniVersoMediaId cniVersoId) {
    return new IdentityVerification(
        IdentityVerificationId.generate(),
        new IdentityVerificationDocuments(cniRectoId, cniVersoId),
        IdentityVerificationReview.pending());
  }

  public static IdentityVerification reconstitute(
      IdentityVerificationId id,
      IdentityVerificationDocuments documents,
      IdentityVerificationReview review) {
    return new IdentityVerification(id, documents, review);
  }

  @Nullable
  public CniRectoMediaId getCniRectoId() {
    return documents.cniRectoId();
  }

  @Nullable
  public CniVersoMediaId getCniVersoId() {
    return documents.cniVersoId();
  }

  public void approve(UserId adminId) {
    this.status = IdentityVerificationStatus.APPROVED;
    this.rejectionReason = null;
    this.verifiedAt = VerifiedAt.now();
    this.verifiedBy = adminId;
  }

  public void reject(RejectionReason reason) {
    this.status = IdentityVerificationStatus.REJECTED;
    this.rejectionReason = reason;
    this.verifiedAt = null;
    this.verifiedBy = null;
  }

  public void resubmit(CniRectoMediaId cniRectoId, CniVersoMediaId cniVersoId) {
    this.status = IdentityVerificationStatus.PENDING;
    this.documents = new IdentityVerificationDocuments(cniRectoId, cniVersoId);
    this.rejectionReason = null;
    this.verifiedAt = null;
    this.verifiedBy = null;
  }
}
