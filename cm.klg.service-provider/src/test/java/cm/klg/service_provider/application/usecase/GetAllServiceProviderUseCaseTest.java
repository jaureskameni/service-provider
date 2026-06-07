package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllServiceProviderUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private ServiceProviderView serviceProviderView;
  @InjectMocks private GetAllServiceProviderUseCase objectUnderTest;

  @Test
  void execute_shouldReturnAllServiceProviders_whenStatusAndLocationAreNull() {
    // Given
    var pagination = new PaginationFetchRequest(10, 0);
    var pageData = new PageData<>(1, java.util.List.of(serviceProviderView));
    when(serviceProviderRepository.loadAllAsView(pagination)).thenReturn(pageData);

    // When
    var result = objectUnderTest.execute(new GetAllServiceProviderUseCase.Command(null, 10, 0));

    // Then
    assertThat(result.serviceProviderViews()).isEqualTo(pageData.elements());
    verify(serviceProviderRepository).loadAllAsView(pagination);
  }

  @Test
  void execute_shouldReturnFilteredByStatus_whenOnlyStatusIsProvided() {
    // Given
    var pagination = new PaginationFetchRequest(10, 0);
    var pageData = new PageData<>(1, java.util.List.of(serviceProviderView));
    when(serviceProviderRepository.loadAllByStatusAsView(
            ServiceProviderStatus.APPROVED, pagination))
        .thenReturn(pageData);

    // When
    var result =
        objectUnderTest.execute(
            new GetAllServiceProviderUseCase.Command(ServiceProviderStatus.APPROVED, 10, 0));

    // Then
    assertThat(result.serviceProviderViews()).isEqualTo(pageData.elements());
    verify(serviceProviderRepository)
        .loadAllByStatusAsView(ServiceProviderStatus.APPROVED, pagination);
  }
}
