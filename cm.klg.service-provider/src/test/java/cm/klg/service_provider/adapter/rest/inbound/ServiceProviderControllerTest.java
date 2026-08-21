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
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PortfolioItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.RejectionReasonDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPublicProfileDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UpdateServiceProviderProfileRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UserServiceDTO;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.AddPortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.DeletePortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.GetAllMyPortfolioUseCase;
import cm.klg.service_provider.application.usecase.GetAllMyServicesUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetProviderPortfolioUseCase;
import cm.klg.service_provider.application.usecase.GetProviderServicesUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderProfileUseCase;
import cm.klg.service_provider.application.usecase.RejectServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdatePortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.UpdateServiceProviderProfileUseCase;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView2;
import cm.klg.service_provider.application.views.UserServiceView;
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
  @Mock private GetAllMyPortfolioUseCase getAllMyPortfolioUseCase;
  @Mock private GetProviderPortfolioUseCase getProviderPortfolioUseCase;
  @Mock private SearchServiceProviderUseCase searchServiceProviderUseCase;
  @Mock private UpdateServiceProviderProfileUseCase updateServiceProviderProfileUseCase;
  @Mock private UpdatePortfolioItemUseCase updatePortfolioItemUseCase;
  @Mock private DeletePortfolioItemUseCase deletePortfolioItemUseCase;
  @Mock private GetAllMyServicesUseCase getAllMyServicesUseCase;
  @Mock private GetProviderServicesUseCase getProviderServicesUseCase;

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
  void addNewService_shouldReturnCreated_whenSuccessful() {
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
            .post("/service-provider/services")
        .then()
            .statusCode(HttpStatus.CREATED.value());
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
    ServiceProviderView1 view =
        new ServiceProviderView1(
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
            LocalDateTime.now());
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
    UUID userId = UUID.randomUUID();
    var view =
        new ServiceProviderView2(
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
            List.of(),
            List.of());
    var useCaseResponse = new GetServiceProviderByIdUseCase.Response(view, true, true);
    var dto = new ServiceProviderPublicProfileDTO().id(spId);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getServiceProviderByIdUseCase.execute(any())).thenReturn(useCaseResponse);
    when(restMapper.toServiceProviderPublicProfileDTO(useCaseResponse)).thenReturn(dto);

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
                .as(ServiceProviderPublicProfileDTO.class);
        // spotless:on

    // Then
    assertThat(result.getId()).isEqualTo(spId);
    verify(getServiceProviderByIdUseCase).execute(any());
    verify(restMapper).toServiceProviderPublicProfileDTO(useCaseResponse);
  }

  @Test
  void getServiceProviderProfile_shouldReturnOk_whenSuccessful() {
    // Given
    var view = mock(ServiceProviderView1.class);
    var dto = new ServiceProviderDTO().id(UUID.randomUUID());

    when(useCaseExecutor.executeQuery(any())).thenReturn(view);
    when(restMapper.toServiceProviderProfileDTO(view)).thenReturn(dto);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
        .when()
                .get("/service-provider/profile")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ServiceProviderDTO.class);
        // spotless:on

    // Then
    assertThat(result.getId()).isEqualTo(dto.getId());
    verify(restMapper).toServiceProviderProfileDTO(view);
  }

  @Test
  void updateServiceProviderProfile_shouldReturnNoContent_whenSuccessful() {
    var request =
        new UpdateServiceProviderProfileRequestDTO()
            .city(UUID.randomUUID())
            .district(UUID.randomUUID())
            .quarter(UUID.randomUUID())
            .about("Updated profile")
            .phoneNumber(new PhoneNumberDTO().countryCode("+237").number("678901234"));

    // spotless:off
    given()
            .standaloneSetup(objectUnderTest)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
        .when()
            .put("/service-provider/profile")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    // spotless:on

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void updatePortfolioItem_shouldReturnNoContent_whenSuccessful() {
    var portfolioId = UUID.randomUUID();
    var request =
        new CreatePortfolioItemRequestDTO()
            .title("Updated title")
            .description("Updated description")
            .mediaId(UUID.randomUUID());

    given()
        .standaloneSetup(objectUnderTest)
        .contentType(MediaType.APPLICATION_JSON)
        .body(request)
        .when()
        .put("/service-provider/portfolio/{portfolioId}", portfolioId)
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void deletePortfolioItem_shouldReturnNoContent_whenSuccessful() {
    var portfolioId = UUID.randomUUID();

    given()
        .standaloneSetup(objectUnderTest)
        .when()
        .delete("/service-provider/portfolio/{portfolioId}", portfolioId)
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void getMyServices_shouldReturnOk_whenSuccessful() {
    var userServiceViews = List.of();
    when(useCaseExecutor.executeQuery(any())).thenReturn(userServiceViews);

    given()
        .standaloneSetup(objectUnderTest)
        .when()
        .get("/service-provider/services")
        .then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  void getProviderServices_shouldReturnOk_whenSuccessful() {
    var providerId = UUID.randomUUID();
    var services = List.of(serviceView());
    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getProviderServicesUseCase.execute(ServiceProviderId.from(providerId)))
        .thenReturn(services);
    when(restMapper.toUserServiceDTOs(services)).thenReturn(List.of(new UserServiceDTO()));

    given()
        .standaloneSetup(objectUnderTest)
        .when()
        .get("/service-provider/{providerId}/services", providerId)
        .then()
        .statusCode(HttpStatus.OK.value());

    verify(getProviderServicesUseCase).execute(ServiceProviderId.from(providerId));
  }

  @Test
  void getAllMyPortfolioItem_shouldReturnOk_whenItemsExist() {
    UUID userId = UUID.randomUUID();
    var views =
        List.of(
            new PortfolioView(
                UUID.randomUUID(), "A", "Desc A", UUID.randomUUID(), LocalDateTime.now()),
            new PortfolioView(
                UUID.randomUUID(), "B", "Desc B", UUID.randomUUID(), LocalDateTime.now()));
    var dtos = List.of(new PortfolioItemDTO().title("A"), new PortfolioItemDTO().title("B"));

    var jwtAuth = mock(JwtAuthenticationToken.class);
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(JwtClaimNames.SUB, userId.toString());
    when(jwtAuth.getTokenAttributes()).thenReturn(attrs);
    SecurityContextHolder.getContext().setAuthentication(jwtAuth);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getAllMyPortfolioUseCase.execute(new UserId(userId))).thenReturn(views);
    when(restMapper.toPortfolioItemDTOs(views)).thenReturn(dtos);

    // When
    @SuppressWarnings("unchecked")
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
        .when()
                .get("/service-provider/portfolio")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PortfolioItemDTO[].class);
        // spotless:on

    // Then
    assertThat(result).hasSize(2);
    assertThat(result[0].getTitle()).isEqualTo("A");
    assertThat(result[1].getTitle()).isEqualTo("B");
    verify(getAllMyPortfolioUseCase).execute(new UserId(userId));
    verify(restMapper).toPortfolioItemDTOs(views);
  }

  @Test
  void getAllMyPortfolioItem_shouldReturnOkEmptyList_whenNoItems() {
    UUID userId = UUID.randomUUID();

    var jwtAuth = mock(JwtAuthenticationToken.class);
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(JwtClaimNames.SUB, userId.toString());
    when(jwtAuth.getTokenAttributes()).thenReturn(attrs);
    SecurityContextHolder.getContext().setAuthentication(jwtAuth);

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getAllMyPortfolioUseCase.execute(new UserId(userId))).thenReturn(List.of());
    when(restMapper.toPortfolioItemDTOs(List.of())).thenReturn(List.of());

    // When
    @SuppressWarnings("unchecked")
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
        .when()
                .get("/service-provider/portfolio")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PortfolioItemDTO[].class);
        // spotless:on

    // Then
    assertThat(result).isEmpty();
    verify(getAllMyPortfolioUseCase).execute(new UserId(userId));
    verify(restMapper).toPortfolioItemDTOs(List.of());
  }

  @Test
  void getProviderPortfolio_shouldReturnOk_whenItemsExist() {
    UUID providerId = UUID.randomUUID();
    var views =
        List.of(
            new PortfolioView(
                UUID.randomUUID(), "A", "Desc A", UUID.randomUUID(), LocalDateTime.now()),
            new PortfolioView(
                UUID.randomUUID(), "B", "Desc B", UUID.randomUUID(), LocalDateTime.now()));
    var dtos = List.of(new PortfolioItemDTO().title("A"), new PortfolioItemDTO().title("B"));

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getProviderPortfolioUseCase.execute(ServiceProviderId.from(providerId))).thenReturn(views);
    when(restMapper.toPortfolioItemDTOs(views)).thenReturn(dtos);

    @SuppressWarnings("unchecked")
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
        .when()
                .get("/service-provider/{serviceProviderId}/portfolio", providerId)
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PortfolioItemDTO[].class);
        // spotless:on

    assertThat(result).hasSize(2);
    assertThat(result[0].getTitle()).isEqualTo("A");
    assertThat(result[1].getTitle()).isEqualTo("B");
    verify(getProviderPortfolioUseCase).execute(ServiceProviderId.from(providerId));
    verify(restMapper).toPortfolioItemDTOs(views);
  }

  @Test
  void getProviderPortfolio_shouldReturnOkEmptyList_whenNoItems() {
    UUID providerId = UUID.randomUUID();

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getProviderPortfolioUseCase.execute(ServiceProviderId.from(providerId)))
        .thenReturn(List.of());
    when(restMapper.toPortfolioItemDTOs(List.of())).thenReturn(List.of());

    @SuppressWarnings("unchecked")
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
        .when()
                .get("/service-provider/{serviceProviderId}/portfolio", providerId)
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PortfolioItemDTO[].class);
        // spotless:on

    assertThat(result).isEmpty();
    verify(getProviderPortfolioUseCase).execute(ServiceProviderId.from(providerId));
    verify(restMapper).toPortfolioItemDTOs(List.of());
  }

  private UserServiceView serviceView() {
    return new UserServiceView(
        new cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView(
            UUID.randomUUID(), "Plumbing", "Repairs", true),
        5,
        UUID.randomUUID(),
        LocalDateTime.now());
  }
}
