package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItem;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeletePortfolioItemUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private DomainEventPublisher domainEventPublisher;
  @InjectMocks private DeletePortfolioItemUseCase objectUnderTest;

  @Test
  void execute_shouldDeletePortfolioItem_whenItemExistsTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId = new PortfolioItemId(UUID.randomUUID());

    var serviceProvider = createServiceProviderWithPortfolioItem(userId, portfolioItemId);
    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    // When
    objectUnderTest.execute(userId, portfolioItemId);

    // Then
    ArgumentCaptor<ServiceProvider> captor = forClass(ServiceProvider.class);
    verify(serviceProviderRepository).loadByUserId(userId);
    verify(serviceProviderRepository).update(captor.capture());

    ServiceProvider updatedProvider = captor.getValue();
    assertThat(updatedProvider.getPortfolioItems()).isEmpty();
  }

  @Test
  void execute_shouldThrowServiceProviderNotFoundException_whenServiceProviderNotFoundTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId = new PortfolioItemId(UUID.randomUUID());

    when(serviceProviderRepository.loadByUserId(userId))
        .thenThrow(new ServiceProviderNotFoundException());

    // When & Then
    assertThatThrownBy(() -> objectUnderTest.execute(userId, portfolioItemId))
        .isInstanceOf(ServiceProviderNotFoundException.class);
    verify(serviceProviderRepository).loadByUserId(userId);
  }

  @Test
  void execute_shouldThrowServiceProviderNotFoundException_whenPortfolioItemNotFoundTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId = new PortfolioItemId(UUID.randomUUID());

    var serviceProvider = createServiceProviderWithoutPortfolioItem(userId);
    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    // When & Then
    assertThatThrownBy(() -> objectUnderTest.execute(userId, portfolioItemId))
        .isInstanceOf(ServiceProviderNotFoundException.class);
    verify(serviceProviderRepository).loadByUserId(userId);
  }

  @Test
  void execute_shouldDeleteCorrectItem_whenMultiplePortfolioItemsExistTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId1 = new PortfolioItemId(UUID.randomUUID());
    var portfolioItemId2 = new PortfolioItemId(UUID.randomUUID());

    var serviceProvider =
        createServiceProviderWithPortfolioItems(userId, portfolioItemId1, portfolioItemId2);
    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    // When
    objectUnderTest.execute(userId, portfolioItemId1);

    // Then
    ArgumentCaptor<ServiceProvider> captor = forClass(ServiceProvider.class);
    verify(serviceProviderRepository).update(captor.capture());

    ServiceProvider updatedProvider = captor.getValue();
    assertThat(updatedProvider.getPortfolioItems()).hasSize(1);
    assertThat(updatedProvider.getPortfolioItems().getFirst().getId()).isEqualTo(portfolioItemId2);
  }

  @Test
  void execute_shouldCallRepositoryLoadAndUpdate_inCorrectOrderTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var portfolioItemId = new PortfolioItemId(UUID.randomUUID());

    var serviceProvider = createServiceProviderWithPortfolioItem(userId, portfolioItemId);
    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    // When
    objectUnderTest.execute(userId, portfolioItemId);

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
            PortfolioItemTitle.from("Title"),
            PortfolioItemDescription.from("Description"),
            new PortfolioItemMediaId(UUID.randomUUID()),
            cm.klg.common.base.domain.CreatedAt.from(java.time.LocalDateTime.now()));
    return serviceProviderWithPortfolioItems(userId, java.util.List.of(portfolioItem));
  }

  private ServiceProvider createServiceProviderWithPortfolioItems(
      UserId userId, PortfolioItemId portfolioItemId1, PortfolioItemId portfolioItemId2) {
    var portfolioItem1 =
        PortfolioItem.reconstitute(
            portfolioItemId1,
            PortfolioItemTitle.from("Title 1"),
            PortfolioItemDescription.from("Description 1"),
            new PortfolioItemMediaId(UUID.randomUUID()),
            cm.klg.common.base.domain.CreatedAt.from(java.time.LocalDateTime.now()));
    var portfolioItem2 =
        PortfolioItem.reconstitute(
            portfolioItemId2,
            PortfolioItemTitle.from("Title 2"),
            PortfolioItemDescription.from("Description 2"),
            new PortfolioItemMediaId(UUID.randomUUID()),
            cm.klg.common.base.domain.CreatedAt.from(java.time.LocalDateTime.now()));
    return serviceProviderWithPortfolioItems(
        userId, java.util.List.of(portfolioItem1, portfolioItem2));
  }

  private ServiceProvider createServiceProviderWithoutPortfolioItem(UserId userId) {
    return serviceProviderWithPortfolioItems(userId, java.util.List.of());
  }

  private ServiceProvider serviceProviderWithPortfolioItems(
      UserId userId, java.util.List<PortfolioItem> portfolioItems) {
    return ServiceProvider.reconstitute(
        cm.klg.service_provider.domain.service_provider.ServiceProviderId.generate(),
        userId,
        new cm.klg.service_provider.domain.service_provider.ProviderContact(
            new cm.klg.service_provider.domain.service_provider.ProviderLocation(
                new cm.klg.service_provider.domain.service_provider.UserCityId(UUID.randomUUID()),
                new cm.klg.service_provider.domain.service_provider.UserDistrictId(
                    UUID.randomUUID()),
                new cm.klg.service_provider.domain.service_provider.UserQuarterId(
                    UUID.randomUUID())),
            cm.klg.service_provider.domain.PhoneNumber.from("+237", "678901234")),
        new cm.klg.service_provider.domain.service_provider.ProviderReview(
            cm.klg.service_provider.domain.service_provider.ServiceProviderStatus.PENDING,
            null,
            null,
            null),
        new cm.klg.service_provider.domain.service_provider.ProviderAudit(
            cm.klg.common.base.domain.CreatedAt.from(java.time.LocalDateTime.now()), null),
        null,
        new cm.klg.service_provider.domain.service_provider.ServiceCollections(
            java.util.List.of(), portfolioItems));
  }
}
