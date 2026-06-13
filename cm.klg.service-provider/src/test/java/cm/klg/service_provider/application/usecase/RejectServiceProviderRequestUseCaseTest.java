package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RejectServiceProviderRequestUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private DomainEventPublisher domainEventPublisher;

  @InjectMocks private RejectServiceProviderRequestUseCase objectUnderTest;

  @Test
  void execute_shouldLoadApproveAndUpdateServiceProvider() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    RejectionReason reason = new RejectionReason("Invalid documents");
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    when(serviceProviderRepository.load(serviceProviderId)).thenReturn(serviceProvider);

    // When
    objectUnderTest.execute(adminId, serviceProviderId, reason);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.REJECTED);
    assertThat(serviceProvider.getRejectionReason()).isEqualTo(reason);
    verify(serviceProviderRepository).load(serviceProviderId);
    verify(serviceProviderRepository).update(serviceProvider);
    ArgumentCaptor<ServiceProviderRejectedEvent> eventCaptor =
        ArgumentCaptor.forClass(ServiceProviderRejectedEvent.class);
    verify(domainEventPublisher).serviceProviderRejectedEvent(eventCaptor.capture());
    assertThat(eventCaptor.getValue().serviceProviderId()).isEqualTo(serviceProvider.getId());
    assertThat(eventCaptor.getValue().userId()).isEqualTo(serviceProvider.getUserId());
    assertThat(eventCaptor.getValue().rejectedBy()).isEqualTo(adminId);
    assertThat(eventCaptor.getValue().rejectionReason()).isEqualTo(reason);
    assertThat(eventCaptor.getValue().rejectedAt()).isNotNull();
  }
}
