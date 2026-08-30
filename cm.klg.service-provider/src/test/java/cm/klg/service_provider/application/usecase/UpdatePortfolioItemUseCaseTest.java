package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItem;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ProviderAudit;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ProviderReview;
import cm.klg.service_provider.domain.service_provider.ServiceCollections;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdatePortfolioItemUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private DomainEventPublisher domainEventPublisher;
  @InjectMocks private UpdatePortfolioItemUseCase objectUnderTest;

  @Test
  void execute_shouldUpdatePortfolioItem_whenItemExistsTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId = new PortfolioItemId(UUID.randomUUID());
    var newTitle = PortfolioItemTitle.from("Updated Title");
    var newDescription = PortfolioItemDescription.from("Updated Description");
    var newMediaId = new PortfolioItemMediaId(UUID.randomUUID());

    var serviceProvider = createServiceProviderWithPortfolioItem(userId, portfolioItemId);
    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    var command =
        new UpdatePortfolioItemUseCase.Command(
            userId, portfolioItemId, newTitle, newDescription, newMediaId);

    // When
    objectUnderTest.execute(command);

    // Then
    ArgumentCaptor<ServiceProvider> captor = forClass(ServiceProvider.class);
    verify(serviceProviderRepository).loadByUserId(userId);
    verify(serviceProviderRepository).update(captor.capture());

    ServiceProvider updatedProvider = captor.getValue();
    assertThat(updatedProvider.getPortfolioItems()).hasSize(1);
    PortfolioItem updatedItem = updatedProvider.getPortfolioItems().getFirst();
    assertThat(updatedItem.getTitle()).isEqualTo(newTitle);
    assertThat(updatedItem.getDescription()).isEqualTo(newDescription);
    assertThat(updatedItem.getMediaId()).isEqualTo(newMediaId);
  }

  @Test
  void execute_shouldThrowServiceProviderNotFoundException_whenServiceProviderNotFoundTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId = new PortfolioItemId(UUID.randomUUID());
    var title = PortfolioItemTitle.from("Title");
    var description = PortfolioItemDescription.from("Description");
    var mediaId = new PortfolioItemMediaId(UUID.randomUUID());

    when(serviceProviderRepository.loadByUserId(userId))
        .thenThrow(new ServiceProviderNotFoundException());

    var command =
        new UpdatePortfolioItemUseCase.Command(
            userId, portfolioItemId, title, description, mediaId);

    // When & Then
    assertThatThrownBy(() -> objectUnderTest.execute(command))
        .isInstanceOf(ServiceProviderNotFoundException.class);
    verify(serviceProviderRepository).loadByUserId(userId);
  }

  @Test
  void execute_shouldThrowServiceProviderNotFoundException_whenPortfolioItemNotFoundTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId = new PortfolioItemId(UUID.randomUUID());
    var title = PortfolioItemTitle.from("Title");
    var description = PortfolioItemDescription.from("Description");
    var mediaId = new PortfolioItemMediaId(UUID.randomUUID());

    var serviceProvider = serviceProviderWithPortfolioItems(userId, List.of());
    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    var command =
        new UpdatePortfolioItemUseCase.Command(
            userId, portfolioItemId, title, description, mediaId);

    // When & Then
    assertThatThrownBy(() -> objectUnderTest.execute(command))
        .isInstanceOf(ServiceProviderNotFoundException.class);
    verify(serviceProviderRepository).loadByUserId(userId);
  }

  @Test
  void execute_shouldCallRepositoryLoadAndUpdate_inCorrectOrderTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId = new PortfolioItemId(UUID.randomUUID());
    var title = PortfolioItemTitle.from("Title");
    var description = PortfolioItemDescription.from("Description");
    var mediaId = new PortfolioItemMediaId(UUID.randomUUID());

    var serviceProvider = createServiceProviderWithPortfolioItem(userId, portfolioItemId);
    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    var command =
        new UpdatePortfolioItemUseCase.Command(
            userId, portfolioItemId, title, description, mediaId);

    // When
    objectUnderTest.execute(command);

    // Then - Verify call order
    var ordered = inOrder(serviceProviderRepository);
    ordered.verify(serviceProviderRepository).loadByUserId(userId);
    ordered.verify(serviceProviderRepository).update(org.mockito.ArgumentMatchers.any());
  }

  private ServiceProvider createServiceProviderWithPortfolioItem(
      UserId userId, PortfolioItemId portfolioItemId) {
    var portfolioItem =
        PortfolioItem.reconstitute(
            portfolioItemId,
            PortfolioItemTitle.from("Original Title"),
            PortfolioItemDescription.from("Original Description"),
            new PortfolioItemMediaId(UUID.randomUUID()),
            CreatedAt.from(java.time.LocalDateTime.now()));
    return serviceProviderWithPortfolioItems(userId, java.util.List.of(portfolioItem));
  }

  private ServiceProvider serviceProviderWithPortfolioItems(
      UserId userId, java.util.List<PortfolioItem> portfolioItems) {
    return ServiceProvider.reconstitute(
        cm.klg.service_provider.domain.service_provider.ServiceProviderId.generate(),
        userId,
        new cm.klg.service_provider.domain.service_provider.ProviderContact(
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            cm.klg.service_provider.domain.PhoneNumber.from("+237", "678901234")),
        new ProviderReview(ServiceProviderStatus.PENDING, null, null, null),
        new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
        null,
        new ServiceCollections(List.of(), portfolioItems));
  }
}
