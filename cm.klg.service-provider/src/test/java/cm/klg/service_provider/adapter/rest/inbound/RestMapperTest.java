package cm.klg.service_provider.adapter.rest.inbound;

import static cm.klg.service_provider.application.views.ServiceProviderViews.*;
import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceCatalogItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import cm.klg.service_provider.application.usecase.GetServiceProviderProfileUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    UUID quarterId = UUID.randomUUID();
    String countryCode = "+237";
    String phoneNumber = "678901234";
    UUID serviceTypeId = UUID.randomUUID();
    int yearOfExperience = 5;
    UUID documentId = UUID.randomUUID();

    ServiceProviderRegisterDTO serviceProviderRegisterDTO =
        new ServiceProviderRegisterDTO()
            .city(cityId)
            .district(districtId)
            .quarter(quarterId)
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
    assertThat(command.location().cityId().value()).isEqualTo(cityId);
    assertThat(command.location().districtId().value()).isEqualTo(districtId);
    assertThat(command.location().quarterId().value()).isEqualTo(quarterId);
    assertThat(command.phoneNumber().countryCode()).isEqualTo(countryCode);
    assertThat(command.phoneNumber().number()).isEqualTo(phoneNumber);
    assertThat(command.serviceTypeId().value()).isEqualTo(serviceTypeId);
    assertThat(command.yearOfExperience().value()).isEqualTo(yearOfExperience);
    assertThat(command.document().value()).isEqualTo(documentId);
  }

  @Test
  void toSearchServiceProviderCommand_shouldMapCorrectly() {
    // Given
    UUID serviceTypeId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID quarterId = UUID.randomUUID();

    // When
    SearchServiceProviderUseCase.Command command =
        objectUnderTest.toSearchServiceProviderCommand(
            serviceTypeId, cityId, districtId, quarterId, 1, 10);

    // Then
    assertThat(command.serviceTypeId().value()).isEqualTo(serviceTypeId);
    assertThat(command.cityId().value()).isEqualTo(cityId);
    assertThat(command.districtId().value()).isEqualTo(districtId);
    assertThat(command.quarterId().value()).isEqualTo(quarterId);
    assertThat(command.status()).isEqualTo(ServiceProviderStatus.APPROVED);
    assertThat(command.limit()).isEqualTo(10);
  }

  @Test
  void toGroupedServiceCatalogDTOs_shouldGroupByCategory() {
    // Given
    ServiceTypeView view1 = new ServiceTypeView(UUID.randomUUID(), "Plumber", "MAINTENANCE", true);
    ServiceTypeView view2 =
        new ServiceTypeView(UUID.randomUUID(), "Electrician", "MAINTENANCE", true);
    ServiceTypeView view3 = new ServiceTypeView(UUID.randomUUID(), "Nanny", "HOME_SERVICES", true);

    // When
    Map<String, List<ServiceCatalogItemDTO>> result =
        objectUnderTest.toGroupedServiceCatalogDTOs(List.of(view1, view2, view3));

    // Then
    assertThat(result).hasSize(2);
    assertThat(result.get("MAINTENANCE")).hasSize(2);
    assertThat(result.get("HOME_SERVICES")).hasSize(1);
  }

  @Test
  void toServiceProviderDTO_shouldMapAllFieldsCorrectly() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID quarterId = UUID.randomUUID();
    UUID documentId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = createdAt.plusDays(1);

    ServiceTypeView serviceTypeView =
        new ServiceTypeView(UUID.randomUUID(), "Plumber", "MAINTENANCE", true);

    UserServiceView userServiceView =
        new UserServiceView(serviceTypeView, 5, documentId, createdAt);

    ServiceProviderView serviceProviderView =
        new ServiceProviderView(
            serviceProviderId,
            userId,
            "John",
            "Doe",
            cityId,
            districtId,
            quarterId,
            null,
            null,
            null,
            null,
            PhoneNumber.from("+237", "678901234"),
            "APPROVED",
            createdAt,
            updatedAt,
            List.of(userServiceView));

    // When
    var result = objectUnderTest.toServiceProviderDTO(serviceProviderView);

    // Then
    assertThat(result.getId()).isEqualTo(serviceProviderId);
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getFirstname()).isEqualTo("John");
    assertThat(result.getLastname()).isEqualTo("Doe");
    assertThat(result.getCity()).isEqualTo(cityId);
    assertThat(result.getDistrict()).isEqualTo(districtId);
    assertThat(result.getQuarter()).isEqualTo(quarterId);
    assertThat(result.getPhoneNumber().getCountryCode()).isEqualTo("+237");
    assertThat(result.getPhoneNumber().getNumber()).isEqualTo("678901234");
    assertThat(result.getServiceProviderStatus()).isEqualTo(ServiceProviderStatusDTO.APPROVED);
    assertThat(result.getCreatedAt()).isEqualTo(createdAt);
    assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    assertThat(result.getUserServices()).hasSize(1);
    var usResult = result.getUserServices().get(0);
    assertThat(usResult.getServiceName()).isEqualTo("Plumber");
    assertThat(usResult.getServiceCategory()).isEqualTo("MAINTENANCE");
    assertThat(usResult.getYearOfExperience()).isEqualTo(5);
    assertThat(usResult.getDocument()).isEqualTo(documentId);
  }

  @Test
  void toPublicServiceProviderProfileDTO_shouldIncludePhoneNumber_whenUserIsClient() {
    // Given
    var profile =
        new ServiceProviderView(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "John",
            "Doe",
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            null,
            null,
            null,
            null,
            PhoneNumber.from("+237", "678901234"),
            "APPROVED",
            LocalDateTime.now(),
            null,
            List.of());

    var response = new GetServiceProviderProfileUseCase.Response(profile, true);

    // When
    var result = objectUnderTest.toServiceProviderProfileDTO(response);

    // Then
    assertThat(result.getPhoneNumber()).isNotNull();
    assertThat(result.getPhoneNumber().getNumber()).isEqualTo("678901234");
  }

  @Test
  void toPublicServiceProviderProfileDTO_shouldHidePhoneNumber_whenUserIsNotClient() {
    // Given
    var profile =
        new ServiceProviderView(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "John",
            "Doe",
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            null,
            null,
            null,
            null,
            PhoneNumber.from("+237", "678901234"),
            "APPROVED",
            LocalDateTime.now(),
            null,
            List.of());

    var response = new GetServiceProviderProfileUseCase.Response(profile, false);

    // When
    var result = objectUnderTest.toServiceProviderProfileDTO(response);

    // Then
    assertThat(result.getPhoneNumber()).isNull();
  }
}
