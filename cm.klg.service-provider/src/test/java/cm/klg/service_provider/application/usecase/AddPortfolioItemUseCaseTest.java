package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItem;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddPortfolioItemUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;

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

    ServiceProvider serviceProvider =
        ServiceProvider.of(
            userId,
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    // When
    objectUnderTest.execute(command);

    // Then
    assertThat(serviceProvider.getPortfolioItems()).hasSize(1);
    PortfolioItem added = serviceProvider.getPortfolioItems().get(0);
    assertThat(added.getTitle()).isEqualTo(title);
    assertThat(added.getDescription()).isEqualTo(description);
    assertThat(added.getMediaId()).isEqualTo(mediaId);
    verify(serviceProviderRepository).loadByUserId(userId);
    verify(serviceProviderRepository).update(serviceProvider);
  }

  @Test
  void execute_shouldAddMultiplePortfolioItems() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());

    ServiceProvider serviceProvider =
        ServiceProvider.of(
            userId,
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    // When
    objectUnderTest.execute(
        new AddPortfolioItemUseCase.Command(
            userId,
            PortfolioItemTitle.from("Project A"),
            PortfolioItemDescription.from("Description A"),
            new PortfolioItemMediaId(UUID.randomUUID())));
    objectUnderTest.execute(
        new AddPortfolioItemUseCase.Command(
            userId,
            PortfolioItemTitle.from("Project B"),
            PortfolioItemDescription.from("Description B"),
            new PortfolioItemMediaId(UUID.randomUUID())));

    // Then
    assertThat(serviceProvider.getPortfolioItems()).hasSize(2);
    verify(serviceProviderRepository, times(2)).update(serviceProvider);
  }
}
