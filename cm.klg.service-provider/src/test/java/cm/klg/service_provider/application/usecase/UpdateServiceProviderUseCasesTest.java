package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.*;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateServiceProviderUseCasesTest {
  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private UserRepository userRepository;
  @Mock private DomainEventPublisher eventPublisher;

  @Test
  void rejectedApplicationResubmissionUpdatesIdentityVerificationAndPreservesServices() {
    var provider = newProvider();
    var oldService =
        UserService.of(
            provider.getId(),
            new ServiceTypeId(UUID.randomUUID()),
            YearOfExperience.from(3),
            UserDocument.from(UUID.randomUUID()));
    provider.addAllUserService(java.util.List.of(oldService));
    provider.reject(new UserId(UUID.randomUUID()), RejectionReason.CNI_INVALID);
    var command = resubmitCommand(provider.getUserId());
    when(serviceProviderRepository.loadByUserId(provider.getUserId())).thenReturn(provider);

    resubmitUseCase().execute(command);

    assertThat(provider.getStatus()).isEqualTo(ServiceProviderStatus.PENDING);
    assertThat(provider.getIdentityVerification().getStatus())
        .isEqualTo(IdentityVerificationStatus.PENDING);
    assertThat(provider.getIdentityVerification().getCniRectoId())
        .isEqualTo(command.identityDocuments().cniRectoId());
    assertThat(provider.getUserServices()).containsExactly(oldService);
    verify(serviceProviderRepository).update(provider);
  }

  @Test
  void approvedProfileImageUpdateChangesDomainAndPersistsProvider() {
    var provider = newProvider();
    provider.approve(new UserId(UUID.randomUUID()));
    var command = approvedCommand(provider.getUserId());
    when(serviceProviderRepository.loadByUserId(provider.getUserId())).thenReturn(provider);

    approvedUseCase().execute(command);

    assertThat(provider.getProfileImageId()).isNotEqualTo(command.profileImageId());
    assertThat(provider.getPendingProfileImageId()).isEqualTo(command.profileImageId());
    assertThat(provider.getProfileImageReviewStatus())
        .isEqualTo(ProfileImageReviewStatus.PENDING_REVIEW);
    assertThat(provider.getUpdatedAt()).isNotNull();
    verify(serviceProviderRepository).update(provider);
  }

  @Test
  void approvedUpdatePropagatesInvalidStatus() {
    var provider = newProvider();
    var command = approvedCommand(provider.getUserId());
    when(serviceProviderRepository.loadByUserId(provider.getUserId())).thenReturn(provider);

    assertThatThrownBy(() -> approvedUseCase().execute(command))
        .isInstanceOf(InvalidServiceProviderStatusException.class);
    verify(serviceProviderRepository, never()).update(provider);
  }

  private ResubmitRejectedServiceProviderUseCase resubmitUseCase() {
    return new ResubmitRejectedServiceProviderUseCase(serviceProviderRepository, userRepository);
  }

  private UpdateApprovedServiceProviderUseCase approvedUseCase() {
    return new UpdateApprovedServiceProviderUseCase(serviceProviderRepository);
  }

  private ServiceProvider newProvider() {
    return ServiceProvider.of(
        new UserId(UUID.randomUUID()),
        location(),
        PhoneNumber.from("+237", "678901234"),
        null,
        IdentityDocuments.of(
            CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
        ProfileImageMediaId.from(UUID.randomUUID()),
        new ArrayList<>());
  }

  private ResubmitRejectedServiceProviderUseCase.Command resubmitCommand(UserId userId) {
    return new ResubmitRejectedServiceProviderUseCase.Command(
        userId,
        providerProfile(),
        IdentityDocuments.of(
            CniRectoMediaId.from(UUID.randomUUID()), CniVersoMediaId.from(UUID.randomUUID())),
        ProfileImageMediaId.from(UUID.randomUUID()));
  }

  private UpdateApprovedServiceProviderUseCase.Command approvedCommand(UserId userId) {
    return new UpdateApprovedServiceProviderUseCase.Command(
        userId, ProfileImageMediaId.from(UUID.randomUUID()));
  }

  private ProviderProfile providerProfile() {
    return new ProviderProfile(
        new ProviderContact(location(), PhoneNumber.from("+237", "699999999")),
        AboutProvider.from("Updated provider profile"));
  }

  private ProviderLocation location() {
    return new ProviderLocation(
        new UserCityId(UUID.randomUUID()), new UserDistrictId(UUID.randomUUID()), null);
  }
}
