package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.favorite.CannotFavoriteOwnProfileException;
import cm.klg.service_provider.domain.favorite.FavoriteProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddServiceProviderToFavoritesUseCaseTest {

  @Mock private FavoriteProviderRepository favoriteProviderRepository;
  @Mock private ServiceProviderRepository serviceProviderRepository;
  @InjectMocks private AddServiceProviderToFavoritesUseCase objectUnderTest;

  @Test
  void execute_shouldInsertFavorite_whenProviderIsApprovedAndNotSelf() {
    UserId userId = new UserId(UUID.randomUUID());
    UserId providerOwnerId = new UserId(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    ServiceProvider serviceProvider = org.mockito.Mockito.mock(ServiceProvider.class);

    when(serviceProviderRepository.load(providerId)).thenReturn(serviceProvider);
    when(serviceProvider.getStatus()).thenReturn(ServiceProviderStatus.APPROVED);
    when(serviceProvider.getUserId()).thenReturn(providerOwnerId);

    objectUnderTest.execute(new AddServiceProviderToFavoritesUseCase.Command(userId, providerId));

    ArgumentCaptor<FavoriteProvider> captor = ArgumentCaptor.forClass(FavoriteProvider.class);
    verify(favoriteProviderRepository).insertIfAbsent(captor.capture());
    assertThat(captor.getValue().getUserId()).isEqualTo(userId);
    assertThat(captor.getValue().getProviderId()).isEqualTo(providerId);
  }

  @Test
  void execute_shouldThrowNotFound_whenProviderIsNotApproved() {
    UserId userId = new UserId(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    ServiceProvider serviceProvider = org.mockito.Mockito.mock(ServiceProvider.class);

    when(serviceProviderRepository.load(providerId)).thenReturn(serviceProvider);
    when(serviceProvider.getStatus()).thenReturn(ServiceProviderStatus.PENDING);

    assertThatThrownBy(
            () ->
                objectUnderTest.execute(
                    new AddServiceProviderToFavoritesUseCase.Command(userId, providerId)))
        .isInstanceOf(ServiceProviderNotFoundException.class);

    verify(favoriteProviderRepository, never()).insertIfAbsent(any());
  }

  @Test
  void execute_shouldThrowBadRequest_whenFavoritingOwnProfile() {
    UserId userId = new UserId(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    ServiceProvider serviceProvider = org.mockito.Mockito.mock(ServiceProvider.class);

    when(serviceProviderRepository.load(providerId)).thenReturn(serviceProvider);
    when(serviceProvider.getStatus()).thenReturn(ServiceProviderStatus.APPROVED);
    when(serviceProvider.getUserId()).thenReturn(userId);

    assertThatThrownBy(
            () ->
                objectUnderTest.execute(
                    new AddServiceProviderToFavoritesUseCase.Command(userId, providerId)))
        .isInstanceOf(CannotFavoriteOwnProfileException.class);

    verify(favoriteProviderRepository, never()).insertIfAbsent(any());
  }
}
