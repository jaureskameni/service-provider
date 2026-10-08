package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.RejectedServiceProviderIdentityView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderIdentityView;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.IdentityVerificationStatus;
import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderStatusException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMyServiceProviderIdentityUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public RejectedServiceProviderIdentityView execute(UserId userId) {
    ServiceProviderIdentityView provider =
        serviceProviderRepository.loadIdentityVerificationByUserId(userId);
    var verification = provider.identityVerification();
    if (verification.status() != IdentityVerificationStatus.REJECTED) {
      throw new InvalidServiceProviderStatusException();
    }

    return new RejectedServiceProviderIdentityView(
        provider.providerId(),
        Objects.requireNonNull(verification.cniRectoId()),
        Objects.requireNonNull(verification.cniVersoId()),
        Objects.requireNonNull(provider.profileImageId()),
        verification.status(),
        Objects.requireNonNull(verification.rejectionReason()));
  }
}
