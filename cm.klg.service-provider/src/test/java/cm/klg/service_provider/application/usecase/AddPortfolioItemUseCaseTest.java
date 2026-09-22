package cm.klg.service_provider.application.usecase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItem;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemAddedEvent;
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
    UserId userId = new UserId(UUID.randomUUID());
    PortfolioItemTitle title = PortfolioItemTitle.from("My project");
    PortfolioItemDescription description = PortfolioItemDescription.from("A great project");
    PortfolioItemMediaId mediaId = new PortfolioItemMediaId(UUID.randomUUID());

    AddPortfolioItemUseCase.Command command =
        new AddPortfolioItemUseCase.Command(userId, title, description, mediaId);

    ServiceProvider serviceProvider = mock(ServiceProvider.class);
    PortfolioItem portfolioItem = mock(PortfolioItem.class);
    ServiceProviderPortfolioItemAddedEvent serviceProviderPortfolioItemAddedEvent =
        mock(ServiceProviderPortfolioItemAddedEvent.class);

    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);
    when(serviceProvider.addPortfolioItem(title, description, mediaId)).thenReturn(portfolioItem);
    when(serviceProvider.toPortfolioItemAddedEvent(portfolioItem))
        .thenReturn(serviceProviderPortfolioItemAddedEvent);

    // When
    objectUnderTest.execute(command);

    // Then
    verify(serviceProvider).addPortfolioItem(title, description, mediaId);
    verify(serviceProviderRepository).update(serviceProvider);
    verify(domainEventPublisher)
        .serviceProviderPortfolioItemAddedEvent(serviceProviderPortfolioItemAddedEvent);
  }
}
