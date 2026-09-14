package cm.klg.service_provider.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamPhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MessagingInboundMapperTest {

  private final MessagingInboundMapper mapper = new MessagingInboundMapperImpl();

  @Test
  void shouldMapUserCreatedEventDTOToCreateNewUserCommand() {
    // Given
    UUID id = UUID.randomUUID();
    var dto = new UamUserCreatedEventDTO();
    dto.setUserId(id);
    dto.setFirstname("John");
    dto.setLastname("Doe");
    dto.setEmail("john.doe@example.com");

    UamPhoneNumberDTO phoneNumberDTO = new UamPhoneNumberDTO();
    phoneNumberDTO.setCountryCode("237");
    phoneNumberDTO.setNumber("699999999");
    dto.setPhoneNumber(phoneNumberDTO);

    // When
    CreateNewUserUseCase.CreateNewUserCommand command = mapper.toCreateUserCommand(dto);

    // Then
    assertThat(command)
        .isNotNull()
        .satisfies(
            c -> {
              assertThat(c.id()).isEqualTo(id);
              assertThat(c.firstname()).isEqualTo("John");
              assertThat(c.lastname()).isEqualTo("Doe");
              assertThat(c.email()).isEqualTo("john.doe@example.com");
              assertThat(c.countryCode()).isEqualTo("237");
              assertThat(c.phoneNumber()).isEqualTo("699999999");
            });
  }
}
