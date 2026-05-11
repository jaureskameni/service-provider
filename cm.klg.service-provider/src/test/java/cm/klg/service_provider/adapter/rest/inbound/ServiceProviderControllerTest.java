package cm.klg.service_provider.adapter.rest.inbound;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProviderId;
import java.util.UUID;
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
}
