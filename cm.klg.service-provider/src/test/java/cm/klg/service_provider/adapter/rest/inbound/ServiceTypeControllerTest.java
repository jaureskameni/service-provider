package cm.klg.service_provider.adapter.rest.inbound;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceCatalogItemDTO;
import cm.klg.service_provider.application.usecase.GetAllServiceTypesUseCase;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView1;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class ServiceTypeControllerTest {
  @Mock private UseCaseExecutor useCaseExecutor;
  @Mock private RestMapper restMapper;
  @Mock private GetAllServiceTypesUseCase getAllServiceTypesUseCase;

  @InjectMocks private ServiceTypeController objectUnderTest;

  @Test
  void getServiceCatalog_shouldReturnOkWithGroupedServices() {
    // Given
    List<ServiceTypeView1> views = List.of();
    var catalogItem =
        new ServiceCatalogItemDTO().id(UUID.randomUUID()).name("Plumber").category("MAINTENANCE");
    Map<String, List<ServiceCatalogItemDTO>> groupedCatalog =
        Map.of("MAINTENANCE", List.of(catalogItem));

    when(useCaseExecutor.executeQuery(any()))
        .thenAnswer(invocation -> invocation.<Supplier<?>>getArgument(0).get());
    when(getAllServiceTypesUseCase.execute()).thenReturn(views);
    when(restMapper.toGroupedServiceCatalogDTOs(views)).thenReturn(groupedCatalog);

    // When
    var result =
        // spotless:off
        given()
                .standaloneSetup(objectUnderTest)
                .contentType(MediaType.APPLICATION_JSON)
        .when()
                .get("/service-catalog")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Map.class);
        // spotless:on

    // Then
    assertThat(result).containsKey("MAINTENANCE");
    verify(getAllServiceTypesUseCase).execute();
    verify(restMapper).toGroupedServiceCatalogDTOs(views);
  }
}
