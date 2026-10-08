package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.IdentityVerificationView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderIdentityView;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.IdentityVerificationStatus;
import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderStatusException;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GetMyServiceProviderIdentityUseCaseTest {
  private final ServiceProviderRepository serviceProviderRepository =
      mock(ServiceProviderRepository.class);
  private final GetMyServiceProviderIdentityUseCase objectUnderTest =
      new GetMyServiceProviderIdentityUseCase(serviceProviderRepository);

  @Test
  void execute_returnsIdentityDocumentsWhenVerificationIsRejected() {
    var userId = UserId.from(UUID.randomUUID());
    var providerId = UUID.randomUUID();
    var rectoId = UUID.randomUUID();
    var versoId = UUID.randomUUID();
    var profileImageId = UUID.randomUUID();
    var verification =
        new IdentityVerificationView(
            UUID.randomUUID(),
            rectoId,
            versoId,
            IdentityVerificationStatus.REJECTED,
            RejectionReason.FACE_MISMATCH,
            null,
            null);
    when(serviceProviderRepository.loadIdentityVerificationByUserId(userId))
        .thenReturn(new ServiceProviderIdentityView(providerId, profileImageId, verification));

    var result = objectUnderTest.execute(userId);

    assertThat(result.providerId()).isEqualTo(providerId);
    assertThat(result.cniRectoId()).isEqualTo(rectoId);
    assertThat(result.cniVersoId()).isEqualTo(versoId);
    assertThat(result.profileImageId()).isEqualTo(profileImageId);
    assertThat(result.status()).isEqualTo(IdentityVerificationStatus.REJECTED);
    assertThat(result.rejectionReason()).isEqualTo(RejectionReason.FACE_MISMATCH);
  }

  @Test
  void execute_rejectsIdentityWhenVerificationIsNotRejected() {
    var userId = UserId.from(UUID.randomUUID());
    var verification =
        new IdentityVerificationView(
            UUID.randomUUID(), null, null, IdentityVerificationStatus.PENDING, null, null, null);
    when(serviceProviderRepository.loadIdentityVerificationByUserId(userId))
        .thenReturn(new ServiceProviderIdentityView(UUID.randomUUID(), null, verification));

    assertThatThrownBy(() -> objectUnderTest.execute(userId))
        .isInstanceOf(InvalidServiceProviderStatusException.class);
  }
}
