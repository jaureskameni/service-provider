package cm.klg.service_provider.adapter.rest.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

  @Test
  void toServiceProviderDTO_shouldMapAllFieldsCorrectly() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID serviceTypeId = UUID.randomUUID();
    UUID documentId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = createdAt.plusDays(1);

    UserServiceView userServiceView = Mockito.mock(UserServiceView.class);
    Mockito.when(userServiceView.getServiceProviderId()).thenReturn(serviceProviderId);
    Mockito.when(userServiceView.getServiceTypeId()).thenReturn(serviceTypeId);
    Mockito.when(userServiceView.getYearOfExperience()).thenReturn(5);
    Mockito.when(userServiceView.getUserDocument()).thenReturn(documentId);
    Mockito.when(userServiceView.getCreatedAt()).thenReturn(createdAt);

    ServiceProviderView1 serviceProviderView = Mockito.mock(ServiceProviderView1.class);
    Mockito.when(serviceProviderView.getId()).thenReturn(serviceProviderId);
    Mockito.when(serviceProviderView.getUserId()).thenReturn(userId);
    Mockito.when(serviceProviderView.getCityId()).thenReturn(cityId);
    Mockito.when(serviceProviderView.getDistrictId()).thenReturn(districtId);
    Mockito.when(serviceProviderView.getPhoneNumber())
        .thenReturn(PhoneNumber.from("+237", "678901234"));
    Mockito.when(serviceProviderView.getStatus()).thenReturn("APPROVED");
    Mockito.when(serviceProviderView.getCreatedAt()).thenReturn(createdAt);
    Mockito.when(serviceProviderView.getUpdatedAt()).thenReturn(updatedAt);
    Mockito.when(serviceProviderView.getUserService()).thenReturn(List.of(userServiceView));

    // When
    var result = objectUnderTest.toServiceProviderDTO(serviceProviderView);

    // Then
    assertThat(result.getId()).isEqualTo(serviceProviderId);
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getCity()).isEqualTo(cityId);
    assertThat(result.getDistrict()).isEqualTo(districtId);
    assertThat(result.getPhoneNumber().getCountryCode()).isEqualTo("+237");
    assertThat(result.getPhoneNumber().getNumber()).isEqualTo("678901234");
    assertThat(result.getServiceProviderStatus()).isEqualTo(ServiceProviderStatusDTO.APPROVED);
    assertThat(result.getCreatedAt()).isEqualTo(createdAt);
    assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    assertThat(result.getUserServices()).hasSize(1);
    assertThat(result.getUserServices().getFirst().getServiceProviderId())
        .isEqualTo(serviceProviderId);
    assertThat(result.getUserServices().getFirst().getDocument()).isEqualTo(documentId);
    assertThat(result.getUserServices().getFirst().getCreatedAt()).isEqualTo(createdAt);
    assertThat(result.getUserServices().getFirst().getServiceType().getId())
        .isEqualTo(serviceTypeId);
    assertThat(result.getUserServices().getFirst().getServiceType().getYearOfExperience())
        .isEqualTo(5);
    assertThat(result.getUserServices().getFirst().getServiceType().getDocument())
        .isEqualTo(documentId);
  }

  @Test
  void toServiceProviderPaginateDTO_shouldMapPageDataCorrectly() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    ServiceProviderView1 serviceProviderView = Mockito.mock(ServiceProviderView1.class);
    Mockito.when(serviceProviderView.getId()).thenReturn(serviceProviderId);
    Mockito.when(serviceProviderView.getUserId()).thenReturn(UUID.randomUUID());
    Mockito.when(serviceProviderView.getCityId()).thenReturn(UUID.randomUUID());
    Mockito.when(serviceProviderView.getDistrictId()).thenReturn(UUID.randomUUID());
    Mockito.when(serviceProviderView.getPhoneNumber())
        .thenReturn(PhoneNumber.from("+237", "678901234"));
    Mockito.when(serviceProviderView.getStatus()).thenReturn("PENDING");
    Mockito.when(serviceProviderView.getCreatedAt()).thenReturn(LocalDateTime.now());
    Mockito.when(serviceProviderView.getUserService()).thenReturn(List.of());
    var pageData = new GetAllServiceProviderUseCase.Response(List.of(serviceProviderView), 7);

    // When
    var result = objectUnderTest.toServiceProviderPaginateDTO(pageData);

    // Then
    assertThat(result.getCount()).isEqualTo(7);
    assertThat(result.getServiceProvider()).hasSize(1);
    assertThat(result.getServiceProvider().getFirst().getId()).isEqualTo(serviceProviderId);
  }
}
