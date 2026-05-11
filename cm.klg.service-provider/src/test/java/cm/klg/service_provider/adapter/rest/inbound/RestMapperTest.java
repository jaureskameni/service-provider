package cm.klg.service_provider.adapter.rest.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestMapperTest {

  private final RestMapper objectUnderTest = new RestMapperImpl();

  @Test
  void toBecomeServiceProviderCommand_shouldMapAllFieldsCorrectly() {
    // Given
    UUID currentUserId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    String countryCode = "+237";
    String phoneNumber = "678901234";
    UUID serviceTypeId = UUID.randomUUID();
    int yearOfExperience = 5;
    UUID documentId = UUID.randomUUID();

    ServiceProviderRegisterDTO serviceProviderRegisterDTO =
        new ServiceProviderRegisterDTO()
            .city(cityId)
            .district(districtId)
            .phoneNumber(new PhoneNumberDTO().countryCode(countryCode).number(phoneNumber))
            .serviceType(
                new ServiceTypeDTO()
                    .id(serviceTypeId)
                    .yearOfExperience(yearOfExperience)
                    .document(documentId));

    // When
    BecomeServiceProviderCommand command =
        objectUnderTest.toBecomeServiceProviderCommand(serviceProviderRegisterDTO, currentUserId);

    // Then
    assertThat(command).isNotNull();
    assertThat(command.userId().value()).isEqualTo(currentUserId);
    assertThat(command.city().value()).isEqualTo(cityId);
    assertThat(command.district().value()).isEqualTo(districtId);
    assertThat(command.phoneNumber().countryCode()).isEqualTo(countryCode);
    assertThat(command.phoneNumber().number()).isEqualTo(phoneNumber);
    assertThat(command.serviceTypeId().value()).isEqualTo(serviceTypeId);
    assertThat(command.yearOfExperience().value()).isEqualTo(yearOfExperience);
    assertThat(command.document().value()).isEqualTo(documentId);
  }

  @Test
  void toBecomeServiceProviderCommand_shouldHandleNullPhoneNumber() {
    // Given
    UUID currentUserId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID serviceTypeId = UUID.randomUUID();
    int yearOfExperience = 5;
    UUID documentId = UUID.randomUUID();

    ServiceProviderRegisterDTO serviceProviderRegisterDTO =
        new ServiceProviderRegisterDTO()
            .city(cityId)
            .district(districtId)
            .phoneNumber(null) // Null phone number
            .serviceType(
                new ServiceTypeDTO()
                    .id(serviceTypeId)
                    .yearOfExperience(yearOfExperience)
                    .document(documentId));

    // When
    BecomeServiceProviderCommand command =
        objectUnderTest.toBecomeServiceProviderCommand(serviceProviderRegisterDTO, currentUserId);

    // Then
    assertThat(command).isNotNull();
    assertThat(command.userId().value()).isEqualTo(currentUserId);
    assertThat(command.city().value()).isEqualTo(cityId);
    assertThat(command.district().value()).isEqualTo(districtId);
    assertThat(command.phoneNumber()).isNull(); // Phone number should be null
    assertThat(command.serviceTypeId().value()).isEqualTo(serviceTypeId);
    assertThat(command.yearOfExperience().value()).isEqualTo(yearOfExperience);
    assertThat(command.document().value()).isEqualTo(documentId);
  }

  @Test
  void toBecomeServiceProviderCommand_shouldHandleNullServiceType() {
    // Given
    UUID currentUserId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    String countryCode = "+237";
    String phoneNumber = "678901234";

    ServiceProviderRegisterDTO serviceProviderRegisterDTO =
        new ServiceProviderRegisterDTO()
            .city(cityId)
            .district(districtId)
            .phoneNumber(new PhoneNumberDTO().countryCode(countryCode).number(phoneNumber))
            .serviceType(null); // Null service type

    // When
    BecomeServiceProviderCommand command =
        objectUnderTest.toBecomeServiceProviderCommand(serviceProviderRegisterDTO, currentUserId);

    // Then
    assertThat(command).isNotNull();
    assertThat(command.userId().value()).isEqualTo(currentUserId);
    assertThat(command.city().value()).isEqualTo(cityId);
    assertThat(command.district().value()).isEqualTo(districtId);
    assertThat(command.phoneNumber().countryCode()).isEqualTo(countryCode);
    assertThat(command.phoneNumber().number()).isEqualTo(phoneNumber);
    assertThat(command.serviceTypeId()).isNull(); // Service type fields should be null
    assertThat(command.yearOfExperience()).isNull();
    assertThat(command.document()).isNull();
  }
}
