package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView3;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GetProfileImageReviewUseCaseTest {
  private final ServiceProviderRepository serviceProviderRepository =
      mock(ServiceProviderRepository.class);
  private final GetProfileImageReviewUseCase objectUnderTest =
      new GetProfileImageReviewUseCase(serviceProviderRepository);

  @Test
  void execute_returnsTheViewLoadedByTheRepository() {
    var id = ServiceProviderId.from(UUID.randomUUID());
    var expectedView = mock(ServiceProviderView3.class);
    when(serviceProviderRepository.loadPendingProfileImageReviewAsView3(id))
        .thenReturn(expectedView);

    var result = objectUnderTest.execute(id);

    assertThat(result).isSameAs(expectedView);
    verify(serviceProviderRepository).loadPendingProfileImageReviewAsView3(id);
  }
}
