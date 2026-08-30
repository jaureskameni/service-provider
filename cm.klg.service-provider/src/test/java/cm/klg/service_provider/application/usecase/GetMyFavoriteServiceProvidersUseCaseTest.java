package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMyFavoriteServiceProvidersUseCaseTest {

  @Mock private FavoriteProviderRepository favoriteProviderRepository;
  @InjectMocks private GetMyFavoriteServiceProvidersUseCase objectUnderTest;

  @Test
  void execute_shouldReturnFavoriteProvidersPage() {
    UserId userId = new UserId(UUID.randomUUID());
    ServiceProviderView1 view = mock(ServiceProviderView1.class);
    when(favoriteProviderRepository.findFavoritesByUserId(
            userId, new PaginationFetchRequest(10, 0)))
        .thenReturn(new PageData<>(1L, List.of(view)));

    var result =
        objectUnderTest.execute(new GetMyFavoriteServiceProvidersUseCase.Command(userId, 10, 0));

    assertThat(result.count()).isEqualTo(1L);
    assertThat(result.serviceProviderViews()).containsExactly(view);
  }
}
