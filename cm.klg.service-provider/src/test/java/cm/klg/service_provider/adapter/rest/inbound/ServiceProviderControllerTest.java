package cm.klg.service_provider.adapter.rest.inbound;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreatePortfolioItemRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.RejectionReasonDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.AddPortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderProfileUseCase;
import cm.klg.service_provider.application.usecase.RejectServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

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
  @Mock private AddPortfolioItemUseCase addPortfolioItemUseCase;
  @Mock private GetServiceProviderProfileUseCase getServiceProviderProfileUseCase;
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

  @Test
  void rejectServiceProvider_shouldReturnNoContent_whenSuccessful() {
    // Given
    UUID spId = UUID.randomUUID();
    RejectionReasonDTO reasonDTO = new RejectionReasonDTO().reason("Invalid data");

    // When & Then
    // spotless:off
    given()
            .standaloneSetup(objectUnderTest)
            .contentType(MediaType.APPLICATION_JSON)
            .body(reasonDTO)
    .when()
            .put("/service-provider/{serviceProviderId}/reject", spId)
    .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    // spotless:on

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void addNewService_shouldReturnNoContent_whenSuccessful() {
    // Given
    var serviceTypeDTO =
        new ServiceTypeDTO().id(UUID.randomUUID()).yearOfExperience(5).document(UUID.randomUUID());

    // When & Then
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

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void addPortfolioItem_shouldReturnNoContent_whenSuccessful() {
    // Given
    var dto =
        new CreatePortfolioItemRequestDTO()
            .title("My Project")
            .description("A great project")
            .mediaId(UUID.randomUUID());

    // When & Then
    // spotless:off
    given()
            .standaloneSetup(objectUnderTest)
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
    .when()
            .post("/service-provider/portfolio")
    .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    // spotless:on

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void approveServiceProvider_shouldReturnNoContent_whenSuccessful() {
    // Given
    UUID spId = UUID.randomUUID();

    // When & Then
    // spotless:off
    given()
            .standaloneSetup(objectUnderTest)
    .when()
            .put("/service-provider/{serviceProviderId}/approve", spId)
    .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    // spotless:on

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void getAllServiceProvider_shouldReturnOk_whenSuccessful() {
    // Given
    ServiceProviderView view =
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
            null,
            "APPROVED",
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of());
    var useCaseResponse = new GetAllServiceProviderUseCase.Response(1L, List.of(view));
    var paginateDTO =
        new ServiceProviderPaginateDTO()
            .count(1L)
            .serviceProvider(List.of(new ServiceProviderDTO().id(view.id())));

    var command = new GetAllServiceProviderUseCase.Command(ServiceProviderStatus.APPROVED, 10, 0);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(restMapper.toGetAllServiceProviderCommand(10, ServiceProviderStatusDTO.APPROVED, 0))
        .thenReturn(command);
    when(getAllServiceProviderUseCase.execute(command)).thenReturn(useCaseResponse);
    when(restMapper.toServiceProviderPaginateDTO(useCaseResponse)).thenReturn(paginateDTO);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
                .queryParam("limit", "10")
                .queryParam("status", "APPROVED")
                .queryParam("page", "0")
        .when()
                .get("/service-provider")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderPaginateDTO.class);
        // spotless:on

    // Then
    assertThat(result.getCount()).isEqualTo(1);
    assertThat(result.getServiceProvider()).hasSize(1);
  }

  @Test
  void getServiceProviderById_shouldReturnOk_whenSuccessful() {
    // Given
    UUID spId = UUID.randomUUID();
    var view =
        new ServiceProviderView(
            spId,
            UUID.randomUUID(),
            "Jane",
            "Smith",
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            null,
            null,
            null,
            null,
            null,
            "APPROVED",
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of());
    var dto = new ServiceProviderDTO().id(spId);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getServiceProviderByIdUseCase.execute(new ServiceProviderId(spId))).thenReturn(view);
    when(restMapper.toServiceProviderDTO(view)).thenReturn(dto);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
        .when()
                .get("/service-provider/{serviceProviderId}", spId)
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderDTO.class);
        // spotless:on

    // Then
    assertThat(result.getId()).isEqualTo(spId);
    verify(getServiceProviderByIdUseCase).execute(new ServiceProviderId(spId));
    verify(restMapper).toServiceProviderDTO(view);
  }

  @Test
  void getServiceProviderProfile_shouldReturnOk_whenSuccessful() {
    // Given
    UUID spId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    var view =
        new ServiceProviderView(
            spId,
            userId,
            "Jane",
            "Smith",
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            null,
            null,
            null,
            null,
            null,
            "APPROVED",
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of());
    var response = new GetServiceProviderProfileUseCase.Response(view, true);
    var dto = new ServiceProviderDTO().id(spId);

    var jwtAuth = mock(JwtAuthenticationToken.class);
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(JwtClaimNames.SUB, userId.toString());
    when(jwtAuth.getTokenAttributes()).thenReturn(attrs);
    SecurityContextHolder.getContext().setAuthentication(jwtAuth);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getServiceProviderProfileUseCase.execute(new ServiceProviderId(spId), new UserId(userId)))
        .thenReturn(response);
    when(restMapper.toServiceProviderProfileDTO(response)).thenReturn(dto);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
        .when()
                .get("/service-provider/{serviceProviderId}/profile", spId)
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderDTO.class);
        // spotless:on

    // Then
    assertThat(result.getId()).isEqualTo(spId);
    verify(getServiceProviderProfileUseCase)
        .execute(new ServiceProviderId(spId), new UserId(userId));
    verify(restMapper).toServiceProviderProfileDTO(response);
  }
}
