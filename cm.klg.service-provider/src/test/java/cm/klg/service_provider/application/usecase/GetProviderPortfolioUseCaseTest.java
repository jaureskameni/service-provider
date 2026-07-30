package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetProviderPortfolioUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @InjectMocks private GetProviderPortfolioUseCase objectUnderTest;

  @Test
  void execute_shouldReturnPortfolioList_whenItemsExist() {
    var providerId = ServiceProviderId.from(UUID.randomUUID());
    var views =
        List.of(
            new PortfolioView(
                UUID.randomUUID(), "Title", "Desc", UUID.randomUUID(), LocalDateTime.now()));

    when(serviceProviderRepository.loadAllProviderPortfolio(providerId)).thenReturn(views);

    var result = objectUnderTest.execute(providerId);

    assertThat(result).isEqualTo(views);
    verify(serviceProviderRepository).loadAllProviderPortfolio(providerId);
  }

  @Test
  void execute_shouldReturnEmptyList_whenNoPortfolioItems() {
    var providerId = ServiceProviderId.from(UUID.randomUUID());

    when(serviceProviderRepository.loadAllProviderPortfolio(providerId)).thenReturn(List.of());

    var result = objectUnderTest.execute(providerId);

    assertThat(result).isEmpty();
    verify(serviceProviderRepository).loadAllProviderPortfolio(providerId);
  }
}
