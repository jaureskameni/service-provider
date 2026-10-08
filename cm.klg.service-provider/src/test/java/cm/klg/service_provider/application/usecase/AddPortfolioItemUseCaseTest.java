package cm.klg.service_provider.application.usecase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddPortfolioItemUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private DomainEventPublisher domainEventPublisher;

  @InjectMocks private AddPortfolioItemUseCase objectUnderTest;

  @Test
  void execute_shouldLoadAddPortfolioItemAndUpdateServiceProvider() {
    // Given
    AddPortfolioItemUseCase.Command command =
        new AddPortfolioItemUseCase.Command(
            new UserId(UUID.randomUUID()),
            new PortfolioItemTitle("title"),
            new PortfolioItemDescription("description"),
            new PortfolioItemMediaId(UUID.randomUUID()));

    ServiceProvider serviceProvider = mock(ServiceProvider.class);

    when(serviceProviderRepository.loadByUserId(command.userId())).thenReturn(serviceProvider);

    // When
    objectUnderTest.execute(command);

    // Then
    verify(serviceProvider)
        .addPortfolioItem(command.title(), command.description(), command.mediaId());
    verify(serviceProviderRepository).update(serviceProvider);
  }
}
