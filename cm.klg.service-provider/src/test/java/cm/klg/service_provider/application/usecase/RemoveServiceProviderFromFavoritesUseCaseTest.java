package cm.klg.service_provider.application.usecase;

import static org.mockito.Mockito.verify;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveServiceProviderFromFavoritesUseCaseTest {

  @Mock private FavoriteProviderRepository favoriteProviderRepository;
  @InjectMocks private RemoveServiceProviderFromFavoritesUseCase objectUnderTest;

  @Test
  void execute_shouldDeleteFavoriteRelation() {
    UserId userId = new UserId(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());

    objectUnderTest.execute(
        new RemoveServiceProviderFromFavoritesUseCase.Command(userId, providerId));

    verify(favoriteProviderRepository).deleteByUserIdAndProviderId(userId, providerId);
  }
}
