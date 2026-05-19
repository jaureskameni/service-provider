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
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.RejectServiceProviderRequestUseCase;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
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

  @InjectMocks private ServiceProviderController objectUnderTest;

  @Test
  void becomeServiceProvider_shouldReturnCreated_whenSuccessful() {
    // Given
    var providerRegisterDTO =
        new ServiceProviderRegisterDTO()
            .city(UUID.randomUUID())
            .district(UUID.randomUUID())
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
  void approveServiceProvider_shouldReturnNoContent_whenSuccessful() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();

    // When
    // spotless:off
    given()
            .standaloneSetup(objectUnderTest)
            .contentType(MediaType.APPLICATION_JSON)
    .when()
            .put("/service-provider/{serviceProviderId}/approve", serviceProviderId)
    .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    // spotless:on

    // Then
    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void addNewService_shouldReturnNoContent_whenSuccessful() {
    // Given
    var serviceTypeDTO =
        new ServiceTypeDTO().id(UUID.randomUUID()).yearOfExperience(5).document(UUID.randomUUID());

    // When
    // spotless:off
    given()
            .standaloneSetup(objectUnderTest)
            .contentType(MediaType.APPLICATION_JSON)
            .body(serviceTypeDTO)
    .when()
            .put("/service-provider/add-service")
    .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    // spotless:on

    // Then
    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void getServiceProviderById_shouldReturnOkWithServiceProvider_whenFound() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    ServiceProviderView1 serviceProviderView = BDDMockito.mock(ServiceProviderView1.class);
    ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO().id(serviceProviderId);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getServiceProviderByIdUseCase.execute(new ServiceProviderId(serviceProviderId)))
        .thenReturn(serviceProviderView);
    when(restMapper.toServiceProviderDTO(serviceProviderView)).thenReturn(serviceProviderDTO);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
                .contentType(MediaType.APPLICATION_JSON)
        .when()
                .get("/service-provider/{serviceProviderId}", serviceProviderId)
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderDTO.class);
        // spotless:on

    // Then
    assertThat(result.getId()).isEqualTo(serviceProviderId);
    verify(getServiceProviderByIdUseCase).execute(new ServiceProviderId(serviceProviderId));
    verify(restMapper).toServiceProviderDTO(serviceProviderView);
  }

  @Test
  void getAllServiceProvider_shouldReturnOkWithAllServiceProviders_whenStatusIsNotProvided() {
    // Given
    ServiceProviderView1 serviceProviderView = BDDMockito.mock(ServiceProviderView1.class);
    ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO().id(UUID.randomUUID());
    var useCaseResponse =
        new GetAllServiceProviderUseCase.Response(List.of(serviceProviderView), 1L);
    var paginateDTO =
        new ServiceProviderPaginateDTO().count(1L).serviceProvider(List.of(serviceProviderDTO));
    var command = new GetAllServiceProviderUseCase.Command(null, 10, 0);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(restMapper.toGetAllServiceProviderCommand(10, null, 0)).thenReturn(command);
    when(getAllServiceProviderUseCase.execute(command)).thenReturn(useCaseResponse);
    when(restMapper.toServiceProviderPaginateDTO(useCaseResponse)).thenReturn(paginateDTO);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
                .contentType(MediaType.APPLICATION_JSON)
        .when()
                .get("/service-provider?limit=10&page=0")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderPaginateDTO.class);
        // spotless:on

    // Then
    assertThat(result.getCount()).isEqualTo(1);
    assertThat(result.getServiceProvider()).hasSize(1);
    assertThat(result.getServiceProvider().getFirst().getId())
        .isEqualTo(serviceProviderDTO.getId());
    verify(restMapper).toGetAllServiceProviderCommand(10, null, 0);
    verify(getAllServiceProviderUseCase).execute(command);
    verify(restMapper).toServiceProviderPaginateDTO(useCaseResponse);
  }

  @Test
  void getAllServiceProvider_shouldReturnOkWithFilteredServiceProviders_whenStatusIsProvided() {
    // Given
    ServiceProviderView1 serviceProviderView = BDDMockito.mock(ServiceProviderView1.class);
    ServiceProviderDTO serviceProviderDTO =
        new ServiceProviderDTO()
            .id(UUID.randomUUID())
            .serviceProviderStatus(ServiceProviderStatusDTO.APPROVED);
    var useCaseResponse =
        new GetAllServiceProviderUseCase.Response(List.of(serviceProviderView), 1L);
    var paginateDTO =
        new ServiceProviderPaginateDTO().count(1L).serviceProvider(List.of(serviceProviderDTO));
    var command = new GetAllServiceProviderUseCase.Command(ServiceProviderStatus.APPROVED, 5, 2);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(restMapper.toGetAllServiceProviderCommand(5, ServiceProviderStatusDTO.APPROVED, 2))
        .thenReturn(command);
    when(getAllServiceProviderUseCase.execute(command)).thenReturn(useCaseResponse);
    when(restMapper.toServiceProviderPaginateDTO(useCaseResponse)).thenReturn(paginateDTO);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("limit", "5")
                .queryParam("status", "APPROVED")
                .queryParam("page", "2")
        .when()
                .get("/service-provider")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderPaginateDTO.class);
        // spotless:on

    // Then
    assertThat(result.getCount()).isEqualTo(1);
    assertThat(result.getServiceProvider().getFirst().getServiceProviderStatus())
        .isEqualTo(ServiceProviderStatusDTO.APPROVED);
    verify(restMapper).toGetAllServiceProviderCommand(5, ServiceProviderStatusDTO.APPROVED, 2);
    verify(getAllServiceProviderUseCase).execute(command);
    verify(restMapper).toServiceProviderPaginateDTO(useCaseResponse);
  }

  @Test
  void getAllServiceProvider_shouldReturnOkWithEmptyList_whenNoServiceProviderFound() {
    // Given
    var useCaseResponse = new GetAllServiceProviderUseCase.Response(List.of(), 0L);
    var paginateDTO = new ServiceProviderPaginateDTO().count(0L).serviceProvider(List.of());
    var command = new GetAllServiceProviderUseCase.Command(ServiceProviderStatus.REJECTED, 10, 0);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(restMapper.toGetAllServiceProviderCommand(10, ServiceProviderStatusDTO.REJECTED, 0))
        .thenReturn(command);
    when(getAllServiceProviderUseCase.execute(command)).thenReturn(useCaseResponse);
    when(restMapper.toServiceProviderPaginateDTO(useCaseResponse)).thenReturn(paginateDTO);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("limit", "10")
                .queryParam("status", "REJECTED")
                .queryParam("page", "0")
        .when()
                .get("/service-provider")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderPaginateDTO.class);
        // spotless:on

    // Then
    assertThat(result.getCount()).isZero();
    assertThat(result.getServiceProvider()).isEmpty();
    verify(restMapper).toGetAllServiceProviderCommand(10, ServiceProviderStatusDTO.REJECTED, 0);
    verify(getAllServiceProviderUseCase).execute(command);
    verify(restMapper).toServiceProviderPaginateDTO(useCaseResponse);
  }
}
