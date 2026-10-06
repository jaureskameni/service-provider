package cm.klg.service_provider.adapter.messaging.outbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderApprovedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderRejectedEventDTO;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OutboxPublisherMapperTest {

  private final OutboxPublisherMapper objectUnderTest = new OutboxPublisherMapperImpl();

  @Test
  void toServiceProviderApprovedEventDTO_shouldMapAllFieldsCorrectly() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID approvedBy = UUID.randomUUID();
    LocalDateTime approvedAt = LocalDateTime.now();

    ServiceProviderApprovedEvent event =
        new ServiceProviderApprovedEvent(
            new ServiceProviderId(serviceProviderId),
            new UserId(userId),
            new UserId(approvedBy),
            approvedAt);

    // When
    ServiceProviderApprovedEventDTO dto = objectUnderTest.toServiceProviderApprovedEventDTO(event);

    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.getServiceProviderId()).isEqualTo(serviceProviderId);
    assertThat(dto.getUserId()).isEqualTo(userId);
    assertThat(dto.getApprovedBy()).isEqualTo(approvedBy);
    assertThat(dto.getApprovedAt()).isEqualTo(approvedAt);
  }

  @Test
  void toServiceProviderRejectedEventDTO_shouldMapAllFieldsCorrectly() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID rejectedBy = UUID.randomUUID();
    RejectionReason reason = RejectionReason.CNI_INVALID;
    LocalDateTime rejectedAt = LocalDateTime.now();
    ServiceProviderRejectedEvent event =
        new ServiceProviderRejectedEvent(
            new ServiceProviderId(serviceProviderId),
            new UserId(userId),
            new UserId(rejectedBy),
            RejectionReason.CNI_INVALID,
            rejectedAt);

    // When
    ServiceProviderRejectedEventDTO dto = objectUnderTest.toServiceProviderRejectedEventDTO(event);

    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.getServiceProviderId()).isEqualTo(serviceProviderId);
    assertThat(dto.getUserId()).isEqualTo(userId);
    assertThat(dto.getRejectedBy()).isEqualTo(rejectedBy);
    assertThat(dto.getReason().name()).isEqualTo(reason.name());
    assertThat(dto.getRejectedAt()).isEqualTo(rejectedAt);
  }
}
