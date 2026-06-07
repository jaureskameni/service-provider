package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchServiceProviderUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private ServiceProviderView serviceProviderView;
  @InjectMocks private SearchServiceProviderUseCase objectUnderTest;

  @Test
  void execute_shouldReturnPrioritizedServiceProviders_whenMandatoryFieldsAreProvided() {
    // Given
    var serviceTypeId = new ServiceTypeId(UUID.randomUUID());
    var cityId = new UserCityId(UUID.randomUUID());
    var districtId = new UserDistrictId(UUID.randomUUID());
    var quarterId = new UserQuarterId(UUID.randomUUID());
    var pagination = new PaginationFetchRequest(10, 0);

    var pageData = new PageData<>(1, List.of(serviceProviderView));

    when(serviceProviderRepository.searchByLocationAndStatus(
            serviceTypeId,
            cityId,
            districtId,
            quarterId,
            ServiceProviderStatus.APPROVED,
            pagination))
        .thenReturn(pageData);

    var command =
        new SearchServiceProviderUseCase.Command(
            serviceTypeId, cityId, districtId, quarterId, ServiceProviderStatus.APPROVED, 10, 0);

    // When
    var result = objectUnderTest.execute(command);

    // Then
    assertThat(result.serviceProviderViews()).isEqualTo(pageData.elements());
    assertThat(result.count()).isEqualTo(pageData.total());

    verify(serviceProviderRepository)
        .searchByLocationAndStatus(
            serviceTypeId,
            cityId,
            districtId,
            quarterId,
            ServiceProviderStatus.APPROVED,
            pagination);
  }

  @Test
  void execute_shouldHandleOptionalDistrictAndQuarter() {
    // Given
    var serviceTypeId = new ServiceTypeId(UUID.randomUUID());
    var cityId = new UserCityId(UUID.randomUUID());
    var pagination = new PaginationFetchRequest(10, 0);

    var pageData = new PageData<>(1, List.of(serviceProviderView));

    when(serviceProviderRepository.searchByLocationAndStatus(
            serviceTypeId, cityId, null, null, null, pagination))
        .thenReturn(pageData);

    var command =
        new SearchServiceProviderUseCase.Command(serviceTypeId, cityId, null, null, null, 10, 0);

    // When
    var result = objectUnderTest.execute(command);

    // Then
    assertThat(result.serviceProviderViews()).isEqualTo(pageData.elements());

    verify(serviceProviderRepository)
        .searchByLocationAndStatus(serviceTypeId, cityId, null, null, null, pagination);
  }
}
