package cm.klg.service_provider.domain.service_provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ServiceProviderTest {

  @Test
  void approve_shouldSetStatusToApproved_whenStatusIsPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    // When
    serviceProvider.approve(adminId);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.APPROVED);
    assertThat(serviceProvider.getApprovedBy()).isEqualTo(adminId);
    assertThat(serviceProvider.getUpdatedAt()).isNotNull();
  }

  @Test
  void reject_shouldSetStatusToRejected_whenStatusIsPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    RejectionReason reason = new RejectionReason("Invalid documents");
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    // When
    serviceProvider.reject(adminId, reason);

    // Then
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.REJECTED);
    assertThat(serviceProvider.getRejectedBy()).isEqualTo(adminId);
    assertThat(serviceProvider.getRejectionReason()).isEqualTo(reason);
    assertThat(serviceProvider.getUpdatedAt()).isNotNull();
  }

  @Test
  void approve_shouldThrow_whenStatusIsNotPending() {
    // Given
    UserId adminId = new UserId(UUID.randomUUID());
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            ServiceProviderId.generate(),
            new UserId(UUID.randomUUID()),
            new ProviderContact(
                new ProviderLocation(
                    new UserCityId(UUID.randomUUID()),
                    new UserDistrictId(UUID.randomUUID()),
                    new UserQuarterId(UUID.randomUUID())),
                new PhoneNumber("+237", "678901234")),
            new ProviderReview(
                ServiceProviderStatus.REJECTED, null, new UserId(UUID.randomUUID()), null),
            new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
            null,
            new ServiceCollections(new ArrayList<>(), new ArrayList<>()));

    assertThatThrownBy(() -> serviceProvider.approve(adminId))
        .isInstanceOf(InvalidServiceProviderStatusException.class);
  }

  @Test
  void getUserServices_shouldReturnUnmodifiableList() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    var userServices = serviceProvider.getUserServices();
    var dummyService =
        UserService.of(
            serviceProvider.getId(),
            new cm.klg.service_provider.domain.service_type.ServiceTypeId(UUID.randomUUID()),
            new YearOfExperience(2),
            new UserDocument(UUID.randomUUID()));

    // When & Then
    assertThatThrownBy(() -> userServices.add(dummyService))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void updatePortfolioItem_shouldThrow_whenPortfolioItemNotFound() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    var nonExistentId = new PortfolioItemId(UUID.randomUUID());
    PortfolioItemTitle title = PortfolioItemTitle.from("Title");
    PortfolioItemDescription description = PortfolioItemDescription.from("Description");
    PortfolioItemMediaId mediaId = new PortfolioItemMediaId(UUID.randomUUID());

    // When & Then
    assertThatThrownBy(
            () -> serviceProvider.updatePortfolioItem(nonExistentId, title, description, mediaId))
        .isInstanceOf(ServiceProviderNotFoundException.class);
  }

  @Test
  void deletePortfolioItem_shouldRemoveItem_whenItemExists() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());
    serviceProvider.approve(new UserId(UUID.randomUUID()));

    serviceProvider.addPortfolioItem(
        PortfolioItemTitle.from("Title"),
        PortfolioItemDescription.from("Description"),
        new PortfolioItemMediaId(UUID.randomUUID()));

    PortfolioItemId itemIdToDelete = serviceProvider.getPortfolioItems().get(0).getId();

    // When
    serviceProvider.deletePortfolioItem(itemIdToDelete);

    // Then
    assertThat(serviceProvider.getPortfolioItems()).isEmpty();
  }

  @Test
  void deletePortfolioItem_shouldThrow_whenPortfolioItemNotFound() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    var nonExistentId = new PortfolioItemId(UUID.randomUUID());

    // When & Then
    assertThatThrownBy(() -> serviceProvider.deletePortfolioItem(nonExistentId))
        .isInstanceOf(ServiceProviderNotFoundException.class);
  }

  @Test
  void deletePortfolioItem_shouldRemoveOnlySpecificItem_whenMultipleItemsExist() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());
    serviceProvider.approve(new UserId(UUID.randomUUID()));

    serviceProvider.addPortfolioItem(
        PortfolioItemTitle.from("Title 1"),
        PortfolioItemDescription.from("Description 1"),
        new PortfolioItemMediaId(UUID.randomUUID()));
    serviceProvider.addPortfolioItem(
        PortfolioItemTitle.from("Title 2"),
        PortfolioItemDescription.from("Description 2"),
        new PortfolioItemMediaId(UUID.randomUUID()));

    PortfolioItemId firstItemId = serviceProvider.getPortfolioItems().getFirst().getId();

    // When
    serviceProvider.deletePortfolioItem(firstItemId);

    // Then
    assertThat(serviceProvider.getPortfolioItems()).hasSize(1);
    assertThat(serviceProvider.getPortfolioItems().get(0).getId()).isNotEqualTo(firstItemId);
  }

  @Test
  void getPortfolioItems_shouldReturnUnmodifiableList() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    var portfolioItems = serviceProvider.getPortfolioItems();

    var portfolioItem =
        PortfolioItem.of(
            PortfolioItemTitle.from("Title"),
            PortfolioItemDescription.from("Description"),
            new PortfolioItemMediaId(UUID.randomUUID()));

    // When & Then
    assertThatThrownBy(() -> portfolioItems.add(portfolioItem))
        .isInstanceOf(UnsupportedOperationException.class);
  }
}
