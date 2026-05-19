package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.utils.PageData;
import cm.klg.service_provider.utils.PaginationFetchRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllServiceProviderUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private ServiceProviderView1 serviceProviderView;
  @InjectMocks private GetAllServiceProviderUseCase objectUnderTest;

  @Test
  void execute_shouldReturnAllServiceProviders_whenStatusIsNullTest() {
    // Given
    var pagination = new PaginationFetchRequest(10, 0);
    var pageData = new PageData<>(1, java.util.List.of(serviceProviderView));
    when(serviceProviderRepository.loadAllAsView1(pagination)).thenReturn(pageData);

    // When
    var result = objectUnderTest.execute(new GetAllServiceProviderUseCase.Command(null, 10, 0));

    // Then

    assertThat(result)
        .satisfies(
            response -> {
              assertThat(response.serviceProviderView1s()).isEqualTo(pageData.elements());
              assertThat(response.count()).isEqualTo(pageData.total());
            });
    verify(serviceProviderRepository).loadAllAsView1(pagination);
  }

  @Test
  void execute_shouldReturnFilteredServiceProviders_whenStatusIsProvidedTest() {
    // Given
    var pagination = new PaginationFetchRequest(10, 1);
    var pageData = new PageData<>(1, java.util.List.of(serviceProviderView));
    when(serviceProviderRepository.loadAllByStatusAsView1(
            ServiceProviderStatus.APPROVED, pagination))
        .thenReturn(pageData);

    // When
    var result =
        objectUnderTest.execute(
            new GetAllServiceProviderUseCase.Command(ServiceProviderStatus.APPROVED, 10, 1));

    // Then
    assertThat(result)
        .satisfies(
            response -> {
              assertThat(response.serviceProviderView1s()).isEqualTo(pageData.elements());
              assertThat(response.count()).isEqualTo(pageData.total());
            });
    verify(serviceProviderRepository)
        .loadAllByStatusAsView1(ServiceProviderStatus.APPROVED, pagination);
  }

  @Test
  void execute_shouldReturnEmptyList_whenNoServiceProviderFoundTest() {
    // Given
    var pagination = new PaginationFetchRequest(5, 0);
    var pageData = new PageData<ServiceProviderView1>(0, java.util.List.of());
    when(serviceProviderRepository.loadAllByStatusAsView1(
            ServiceProviderStatus.REJECTED, pagination))
        .thenReturn(pageData);

    // When
    var result =
        objectUnderTest.execute(
            new GetAllServiceProviderUseCase.Command(ServiceProviderStatus.REJECTED, 5, 0));

    // Then

    assertThat(result)
        .satisfies(
            response -> {
              assertThat(response.serviceProviderView1s()).isEqualTo(pageData.elements());
              assertThat(response.count()).isEqualTo(pageData.total());
            });
    verify(serviceProviderRepository)
        .loadAllByStatusAsView1(ServiceProviderStatus.REJECTED, pagination);
  }
}
