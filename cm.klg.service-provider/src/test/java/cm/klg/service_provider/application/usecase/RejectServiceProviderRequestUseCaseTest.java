package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RejectServiceProviderRequestUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;

  @InjectMocks private RejectServiceProviderRequestUseCase objectUnderTest;

  @Test
  void execute_shouldLoadApproveAndUpdateServiceProvider() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            new ArrayList<>());

    when(serviceProviderRepository.load(serviceProviderId)).thenReturn(serviceProvider);

    // When
    objectUnderTest.execute(adminId, serviceProviderId);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.REJECTED);
    verify(serviceProviderRepository).load(serviceProviderId);
    verify(serviceProviderRepository).update(serviceProvider);
  }
}
