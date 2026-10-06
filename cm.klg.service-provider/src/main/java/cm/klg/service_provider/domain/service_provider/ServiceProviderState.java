package cm.klg.service_provider.domain.service_provider;

public record ServiceProviderState(
    ProviderProfile profile,
    ProviderReview review,
    ProviderAudit audit,
    IdentityVerification identityVerification,
    ProviderImages images,
    ServiceCollections collections) {

  public static ServiceProviderState from(
      ProviderProfile profile,
      ProviderReview review,
      ProviderAudit audit,
      IdentityDocuments documents,
      ProviderImages images,
      ServiceCollections collections) {
    IdentityVerificationStatus verificationStatus =
        IdentityVerificationStatus.valueOf(review.status().name());
    var verificationReview =
        new IdentityVerificationReview(
            verificationStatus,
            review.rejectionReason(),
            verificationStatus == IdentityVerificationStatus.APPROVED ? VerifiedAt.now() : null,
            verificationStatus == IdentityVerificationStatus.APPROVED ? review.approvedBy() : null);
    var verification =
        IdentityVerification.reconstitute(
            IdentityVerificationId.generate(),
            new IdentityVerificationDocuments(documents.cniRectoId(), documents.cniVersoId()),
            verificationReview);
    return new ServiceProviderState(profile, review, audit, verification, images, collections);
  }
}
