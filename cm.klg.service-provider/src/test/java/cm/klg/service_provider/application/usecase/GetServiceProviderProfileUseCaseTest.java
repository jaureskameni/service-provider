package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetServiceProviderProfileUseCaseTest {
  @Mock private ServiceProviderRepository serviceProviderRepository;
  @InjectMocks private GetServiceProviderProfileUseCase objectUnderTest;

  @Test
  void execute_shouldReturnProfileView() {
    var currentUserId = new UserId(UUID.randomUUID());
    var profileView = mock(ServiceProviderView1.class);
    when(serviceProviderRepository.loadAsView1(currentUserId)).thenReturn(profileView);

    var result = objectUnderTest.execute(currentUserId);

    assertThat(result).isEqualTo(profileView);
  }
}
