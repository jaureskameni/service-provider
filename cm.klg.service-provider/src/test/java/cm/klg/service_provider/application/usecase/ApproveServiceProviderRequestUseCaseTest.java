package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.user.User;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApproveServiceProviderRequestUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private UserRepository userRepository;
  @Mock private DomainEventPublisher domainEventPublisher;

  @InjectMocks private ApproveServiceProviderRequestUseCase objectUnderTest;

  @Test
  void execute_shouldLoadApproveAndUpdateServiceProvider_AndEmitEvent() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    UserId providerUserId = new UserId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            providerUserId,
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            new ArrayList<>());

    User user = org.mockito.Mockito.mock(User.class);

    when(serviceProviderRepository.load(serviceProviderId)).thenReturn(serviceProvider);
    when(userRepository.load(providerUserId)).thenReturn(user);

    // When
    objectUnderTest.execute(adminId, serviceProviderId);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.APPROVED);
    verify(serviceProviderRepository).load(serviceProviderId);
    verify(serviceProviderRepository).update(serviceProvider);
    verify(userRepository).load(providerUserId);
    verify(domainEventPublisher).serviceProviderApprovedEvent(any());
  }
}
