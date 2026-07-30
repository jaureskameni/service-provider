package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.domain.UserId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllMyPortfolioUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @InjectMocks private GetAllMyPortfolioUseCase objectUnderTest;

  @Test
  void execute_shouldReturnPortfolioList_whenItemsExist() {
    var userId = UserId.from(UUID.randomUUID());
    var views =
        List.of(
            new PortfolioView(
                UUID.randomUUID(), "Title", "Desc", UUID.randomUUID(), LocalDateTime.now()));

    when(serviceProviderRepository.loadAllPortfolio(userId)).thenReturn(views);

    var result = objectUnderTest.execute(userId);

    assertThat(result).isEqualTo(views);
    verify(serviceProviderRepository).loadAllPortfolio(userId);
  }

  @Test
  void execute_shouldReturnEmptyList_whenNoPortfolioItems() {
    var userId = UserId.from(UUID.randomUUID());

    when(serviceProviderRepository.loadAllPortfolio(userId)).thenReturn(List.of());

    var result = objectUnderTest.execute(userId);

    assertThat(result).isEmpty();
    verify(serviceProviderRepository).loadAllPortfolio(userId);
  }
}
