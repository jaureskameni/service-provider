package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetServiceProviderProfileUseCaseTest {
  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private ProviderClientRepository providerClientRepository;
  @InjectMocks private GetServiceProviderProfileUseCase objectUnderTest;

  @Test
  void execute_shouldReturnProfileWithIsClientTrue_whenUserIsClient() {
    // Given
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    var currentUserId = new UserId(UUID.randomUUID());
    var profile = createProfile(serviceProviderId.value());

    when(serviceProviderRepository.loadProfile(serviceProviderId)).thenReturn(profile);
    when(providerClientRepository.existsByUserIdAndProviderId(currentUserId, serviceProviderId))
        .thenReturn(true);

    // When
    var result = objectUnderTest.execute(serviceProviderId, currentUserId);

    // Then
    assertThat(result.profile()).isEqualTo(profile);
    assertThat(result.isClient()).isTrue();
  }

  @Test
  void execute_shouldReturnProfileWithIsClientFalse_whenUserIsNotClient() {
    // Given
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    var currentUserId = new UserId(UUID.randomUUID());
    var profile = createProfile(serviceProviderId.value());

    when(serviceProviderRepository.loadProfile(serviceProviderId)).thenReturn(profile);
    when(providerClientRepository.existsByUserIdAndProviderId(currentUserId, serviceProviderId))
        .thenReturn(false);

    // When
    var result = objectUnderTest.execute(serviceProviderId, currentUserId);

    // Then
    assertThat(result.profile()).isEqualTo(profile);
    assertThat(result.isClient()).isFalse();
  }

  @Test
  void execute_shouldReturnProfileWithIsClientFalse_whenNoCurrentUser() {
    // Given
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    var profile = createProfile(serviceProviderId.value());
    when(serviceProviderRepository.loadProfile(serviceProviderId)).thenReturn(profile);

    // When
    var result = objectUnderTest.execute(serviceProviderId, null);

    // Then
    assertThat(result.profile()).isEqualTo(profile);
    assertThat(result.isClient()).isFalse();
  }

  private ServiceProviderView createProfile(UUID id) {
    return new ServiceProviderView(
        id,
        UUID.randomUUID(),
        "Firstname",
        "Lastname",
        UUID.randomUUID(),
        UUID.randomUUID(),
        null,
        null,
        null,
        new PhoneNumber("+237", "678901234"),
        ServiceProviderStatus.APPROVED.name(),
        LocalDateTime.now(),
        null,
        Collections.emptyList());
  }
}
