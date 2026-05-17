package cm.klg.service_provider.domain.service_provider;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ServiceProviderTest {

  @Test
  void approve_shouldSetStatusToApproved_whenStatusIsPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new UserCityId(UUID.randomUUID()),
            new UserDistrictId(UUID.randomUUID()),
            new PhoneNumber("+237", "678901234"),
            new ArrayList<>());

    // When
    serviceProvider.approve(adminId);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.APPROVED);
    assertThat(serviceProvider.getApprovedBy()).isEqualTo(adminId);
    assertThat(serviceProvider.getUpdatedAt()).isNotNull();
  }

  @Test
  void reject_shouldSetStatusToRejected_whenStatusIsPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new UserCityId(UUID.randomUUID()),
            new UserDistrictId(UUID.randomUUID()),
            new PhoneNumber("+237", "678901234"),
            new ArrayList<>());

    // When
    serviceProvider.reject(adminId);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.REJECTED);
    assertThat(serviceProvider.getRejectedBy()).isEqualTo(adminId);
    assertThat(serviceProvider.getUpdatedAt()).isNotNull();
  }

  @Test
  void approve_shouldNotChangeStatus_whenStatusIsNotPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            ServiceProviderId.generate(),
            new UserId(UUID.randomUUID()),
            new ProviderContact(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new PhoneNumber("+237", "678901234")),
            new ProviderReview(ServiceProviderStatus.REJECTED, null, new UserId(UUID.randomUUID())),
            new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
            new ArrayList<>());

    // When
    serviceProvider.approve(adminId);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.REJECTED);
    assertThat(serviceProvider.getApprovedBy()).isNull();
  }
}
