package cm.klg.service_provider.adapter.messaging.outbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderApprovedEventDTO;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OutboxWriterMapperTest {

  private final OutboxWriterMapper objectUnderTest = new OutboxWriterMapperImpl();

  @Test
  void toServiceProviderApprovedEventDTO_shouldMapAllFieldsCorrectly() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    String lastname = "Nguematcha";
    String firstname = "Kameni";
    String email = "kameni@example.com";
    String countryCode = "+237";
    String number = "678901234";
    LocalDateTime approvedAt = LocalDateTime.now();

    User user =
        User.reconstitute(
            new UserId(userId),
            new UserProfile(
                Firstname.from(firstname),
                Lastname.from(lastname),
                EmailAddress.from(email),
                PhoneNumber.from(countryCode, number)),
            false,
            CreatedAt.from(LocalDateTime.now().minusDays(1)));

    ServiceProviderApprovedEvent event =
        new ServiceProviderApprovedEvent(
            new ServiceProviderId(serviceProviderId), new UserId(userId), user, approvedAt);

    // When
    ServiceProviderApprovedEventDTO dto = objectUnderTest.toServiceProviderApprovedEventDTO(event);

    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.getServiceProviderId()).isEqualTo(serviceProviderId);
    assertThat(dto.getUserId()).isEqualTo(userId);
    assertThat(dto.getLastname()).isEqualTo(lastname);
    assertThat(dto.getFirstname()).isEqualTo(firstname);
    assertThat(dto.getEmail()).isEqualTo(email);
    assertThat(dto.getPhoneNumber().getCountryCode()).isEqualTo(countryCode);
    assertThat(dto.getPhoneNumber().getNumber()).isEqualTo(number);
    assertThat(dto.getApprovedAt()).isEqualTo(approvedAt);
  }
}
