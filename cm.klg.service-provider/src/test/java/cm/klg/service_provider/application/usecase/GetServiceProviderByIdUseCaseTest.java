package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView2;
import cm.klg.service_provider.domain.UserId;
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
  @Mock private ProviderClientRepository providerClientRepository;
  @Mock private FavoriteProviderRepository favoriteProviderRepository;
  @InjectMocks private GetServiceProviderByIdUseCase objectUnderTest;

  @Test
  void execute_shouldReturnResponseWithFlagsTrue_whenUserIsClientAndFavorite() {
    var providerId = new ServiceProviderId(UUID.randomUUID());
    var userId = new UserId(UUID.randomUUID());
    var view = mock(ServiceProviderView2.class);
    var command = new GetServiceProviderByIdUseCase.Command(providerId, userId);

    when(serviceProviderRepository.loadApprovedAsView2(providerId)).thenReturn(view);
    when(providerClientRepository.existsByUserIdAndProviderId(userId, providerId)).thenReturn(true);
    when(favoriteProviderRepository.existsByUserIdAndProviderId(userId, providerId))
        .thenReturn(true);

    var result = objectUnderTest.execute(command);

    assertThat(result.serviceProviderView2()).isEqualTo(view);
    assertThat(result.isClient()).isTrue();
    assertThat(result.isFavorite()).isTrue();
  }

  @Test
  void execute_shouldReturnResponseWithFlagsFalse_whenUserIsNotClientNorFavorite() {
    var providerId = new ServiceProviderId(UUID.randomUUID());
    var userId = new UserId(UUID.randomUUID());
    var view = mock(ServiceProviderView2.class);
    var command = new GetServiceProviderByIdUseCase.Command(providerId, userId);

    when(serviceProviderRepository.loadApprovedAsView2(providerId)).thenReturn(view);
    when(providerClientRepository.existsByUserIdAndProviderId(userId, providerId))
        .thenReturn(false);
    when(favoriteProviderRepository.existsByUserIdAndProviderId(userId, providerId))
        .thenReturn(false);

    var result = objectUnderTest.execute(command);

    assertThat(result.serviceProviderView2()).isEqualTo(view);
    assertThat(result.isClient()).isFalse();
    assertThat(result.isFavorite()).isFalse();
  }

  @Test
  void execute_shouldReturnFlagsFalse_whenUserIdIsNull() {
    var providerId = new ServiceProviderId(UUID.randomUUID());
    var view = mock(ServiceProviderView2.class);
    var command = new GetServiceProviderByIdUseCase.Command(providerId, null);

    when(serviceProviderRepository.loadApprovedAsView2(providerId)).thenReturn(view);

    var result = objectUnderTest.execute(command);

    assertThat(result.serviceProviderView2()).isEqualTo(view);
    assertThat(result.isClient()).isFalse();
    assertThat(result.isFavorite()).isFalse();
    verifyNoInteractions(providerClientRepository, favoriteProviderRepository);
  }
}
