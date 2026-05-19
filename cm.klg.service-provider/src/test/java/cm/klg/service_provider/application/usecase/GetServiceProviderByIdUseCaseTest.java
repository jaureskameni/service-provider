package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetServiceProviderByIdUseCaseTest {
  @Mock private ServiceProviderRepository serviceProviderRepository;
  @InjectMocks private GetServiceProviderByIdUseCase objectUnderTest;

  @Test
  void execute_shouldReturnServiceProviderView_whenFound() {
    // Given
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    var serviceProviderView = mock(ServiceProviderView1.class);
    when(serviceProviderRepository.loadAsView1(serviceProviderId)).thenReturn(serviceProviderView);

    // When
    var result = objectUnderTest.execute(serviceProviderId);

    // Then
    assertThat(result).isEqualTo(serviceProviderView);
  }
}
