package cm.klg.service_provider.adapter.rest.inbound;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.RejectServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class ServiceProviderControllerTest {
  @Mock private UseCaseExecutor useCaseExecutor;
  @Mock private RestMapper restMapper;
  @Mock private BecomeServiceProviderUseCase becomeServiceProviderUseCase;
  @Mock private GetAllServiceProviderUseCase getAllServiceProviderUseCase;
  @Mock private GetServiceProviderByIdUseCase getServiceProviderByIdUseCase;
  @Mock private ApproveServiceProviderRequestUseCase approveServiceProviderRequestUseCase;
  @Mock private RejectServiceProviderRequestUseCase rejectServiceProviderRequestUseCase;
  @Mock private AddNewServiceUseCase addNewServiceUseCase;
  @Mock private SearchServiceProviderUseCase searchServiceProviderUseCase;

  @InjectMocks private ServiceProviderController objectUnderTest;

  @Test
  void becomeServiceProvider_shouldReturnCreated_whenSuccessful() {
    // Given
    var providerRegisterDTO =
        new ServiceProviderRegisterDTO()
            .city(UUID.randomUUID())
            .district(UUID.randomUUID())
            .quarter(UUID.randomUUID())
            .phoneNumber(new PhoneNumberDTO().number("1259863").countryCode("+237"))
            .serviceType(
                new ServiceTypeDTO()
                    .id(UUID.randomUUID())
                    .yearOfExperience(5)
                    .document(UUID.randomUUID()));
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());

    BDDMockito.given(useCaseExecutor.executeCommand(any())).willReturn(serviceProviderId);

    // When
    var result =
        // spotless:off
                given()
                        .standaloneSetup(objectUnderTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(providerRegisterDTO)
                .when()
                        .post("/service-provider")
                .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(CreationResponseDTO.class);
        // spotless:on
    // Then
    assertThat(result.getNewId()).isEqualTo(serviceProviderId.value());
  }

  @Test
  void searchServiceProviders_shouldReturnOkWithPrioritizedProviders_whenMandatoryFieldsProvided() {
    // Given
    UUID serviceTypeId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID quarterId = UUID.randomUUID();

    ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO().id(UUID.randomUUID());
    var useCaseResponse = new SearchServiceProviderUseCase.Response(1L, List.of());
    var paginateDTO =
        new ServiceProviderPaginateDTO().count(1L).serviceProvider(List.of(serviceProviderDTO));

    var command =
        new SearchServiceProviderUseCase.Command(
            new ServiceTypeId(serviceTypeId),
            new UserCityId(cityId),
            new UserDistrictId(districtId),
            new UserQuarterId(quarterId),
            ServiceProviderStatus.APPROVED,
            10,
            0);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(restMapper.toSearchServiceProviderCommand(
            serviceTypeId, cityId, districtId, quarterId, 0, 10))
        .thenReturn(command);
    when(searchServiceProviderUseCase.execute(command)).thenReturn(useCaseResponse);
    when(restMapper.toServiceProviderPaginateDTO(useCaseResponse)).thenReturn(paginateDTO);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("serviceTypeId", serviceTypeId.toString())
                .queryParam("cityId", cityId.toString())
                .queryParam("districtId", districtId.toString())
                .queryParam("quarterId", quarterId.toString())
                .queryParam("page", "0")
                .queryParam("limit", "10")
        .when()
                .get("/service-provider/search")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderPaginateDTO.class);
        // spotless:on

    // Then
    assertThat(result.getCount()).isEqualTo(1);
    assertThat(result.getServiceProvider()).hasSize(1);
    verify(restMapper)
        .toSearchServiceProviderCommand(serviceTypeId, cityId, districtId, quarterId, 0, 10);
    verify(searchServiceProviderUseCase).execute(command);
  }
}
