package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JpaMapperPortfolioAndServicesTest {

  private final JpaMapper objectUnderTest = new JpaMapperImpl();

  @Nested
  class PortfolioViewMapping {

    @Test
    void toPortfolioView_shouldMapAllFields() {
      // Given
      var id = UUID.randomUUID();
      var title = "Portfolio Title";
      var description = "Portfolio Description";
      var mediaId = UUID.randomUUID();
      var createdAt = LocalDateTime.of(2024, 1, 15, 10, 30);

      var portfolioItemJpa = new PortfolioItemJpa();
      portfolioItemJpa.setId(id);
      portfolioItemJpa.setTitle(title);
      portfolioItemJpa.setDescription(description);
      portfolioItemJpa.setMediaId(mediaId);
      portfolioItemJpa.setCreatedAt(createdAt);

      // When
      PortfolioView result = objectUnderTest.toPortfolioView(portfolioItemJpa);

      // Then
      assertThat(result.id()).isEqualTo(id);
      assertThat(result.title()).isEqualTo(title);
      assertThat(result.description()).isEqualTo(description);
      assertThat(result.mediaId()).isEqualTo(mediaId);
      assertThat(result.createdAt()).isEqualTo(createdAt);
    }

    @Test
    void toPortfolioView_shouldMapEmptyStrings() {
      // Given
      var id = UUID.randomUUID();
      var portfolioItemJpa = new PortfolioItemJpa();
      portfolioItemJpa.setId(id);
      portfolioItemJpa.setTitle("");
      portfolioItemJpa.setDescription("");
      portfolioItemJpa.setMediaId(UUID.randomUUID());
      portfolioItemJpa.setCreatedAt(LocalDateTime.now());

      // When
      PortfolioView result = objectUnderTest.toPortfolioView(portfolioItemJpa);

      // Then
      assertThat(result.title()).isEmpty();
      assertThat(result.description()).isEmpty();
    }

    @Test
    void toPortfolioView_shouldPreserveAllInformation() {
      // Given
      var portfolioItemJpa = createPortfolioItemJpa();

      // When
      PortfolioView result = objectUnderTest.toPortfolioView(portfolioItemJpa);

      // Then
      assertThat(result).isNotNull();
      assertThat(result.id()).isNotNull();
      assertThat(result.title()).isNotNull();
      assertThat(result.description()).isNotNull();
      assertThat(result.mediaId()).isNotNull();
      assertThat(result.createdAt()).isNotNull();
    }
  }

  @Nested
  class UserServiceConversion {

    @Test
    void toUserServiceDomain_shouldConvertJpaListToDomainList() {
      // Given
      var serviceProviderId = UUID.randomUUID();
      var serviceTypeId = UUID.randomUUID();
      var userServices =
          List.of(
              createUserServiceJpa(serviceProviderId, serviceTypeId, 5),
              createUserServiceJpa(serviceProviderId, serviceTypeId, 3));

      // When
      var result = objectUnderTest.toUserServiceDomain(userServices);

      // Then
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getYearOfExperience().value()).isEqualTo(5);
      assertThat(result.get(1).getYearOfExperience().value()).isEqualTo(3);
    }

    @Test
    void toUserServiceDomain_shouldReturnEmptyList_whenInputIsEmpty() {
      // Given
      var emptyList = new ArrayList<UserServiceJpa>();

      // When
      var result = objectUnderTest.toUserServiceDomain(emptyList);

      // Then
      assertThat(result).isEmpty();
    }

    @Test
    void toUserServiceDomain_shouldConvertEachItemCorrectly() {
      // Given
      var serviceProviderId = UUID.randomUUID();
      var serviceTypeId = UUID.randomUUID();
      var createdAt = LocalDateTime.of(2024, 1, 15, 10, 30);

      var userServiceJpa = new UserServiceJpa();
      userServiceJpa.setId(userServiceJpaId(serviceProviderId, serviceTypeId));
      userServiceJpa.setYearOfExperience(7);
      userServiceJpa.setUserDocument(UUID.randomUUID());
      userServiceJpa.setCreatedAt(createdAt);

      var userServices = List.of(userServiceJpa);

      // When
      var result = objectUnderTest.toUserServiceDomain(userServices);

      // Then
      assertThat(result).hasSize(1);
      assertThat(result.get(0).getYearOfExperience().value()).isEqualTo(7);
      assertThat(result.get(0).getUserDocument().value())
          .isEqualTo(userServiceJpa.getUserDocument());
    }
  }

  @Nested
  class PortfolioItemSync {

    @Test
    void addPortfolioItemJpa_shouldAddPortfolioItem_toServiceProvider() {
      // Given
      var serviceProviderJpa = new ServiceProviderJpa();
      var portfolioItem = createPortfolioItem();

      // When
      objectUnderTest.addPortfolioItemJpa(serviceProviderJpa, portfolioItem);

      // Then
      assertThat(serviceProviderJpa.getPortfolioItems()).hasSize(1);
      assertThat(serviceProviderJpa.getPortfolioItems().get(0).getServiceProvider())
          .isEqualTo(serviceProviderJpa);
    }

    @Test
    void addPortfolioItemJpa_shouldAddMultiplePortfolioItems() {
      // Given
      var serviceProviderJpa = new ServiceProviderJpa();
      var portfolioItem1 = createPortfolioItem();
      var portfolioItem2 = createPortfolioItem();

      // When
      objectUnderTest.addPortfolioItemJpa(serviceProviderJpa, portfolioItem1);
      objectUnderTest.addPortfolioItemJpa(serviceProviderJpa, portfolioItem2);

      // Then
      assertThat(serviceProviderJpa.getPortfolioItems()).hasSize(2);
      assertThat(serviceProviderJpa.getPortfolioItems())
          .allMatch(item -> item.getServiceProvider().equals(serviceProviderJpa));
    }

    @Test
    void addPortfolioItemJpa_shouldPreservePortfolioItemFields() {
      // Given
      var serviceProviderJpa = new ServiceProviderJpa();
      var title = "Project Title";
      var description = "Project Description";
      var mediaId = UUID.randomUUID();

      var portfolioItem =
          cm.klg.service_provider.domain.service_provider.PortfolioItem.reconstitute(
              new cm.klg.service_provider.domain.service_provider.PortfolioItemId(
                  UUID.randomUUID()),
              PortfolioItemTitle.from(title),
              PortfolioItemDescription.from(description),
              new PortfolioItemMediaId(mediaId),
              CreatedAt.from(LocalDateTime.now()));

      // When
      objectUnderTest.addPortfolioItemJpa(serviceProviderJpa, portfolioItem);

      // Then
      var addedItem = serviceProviderJpa.getPortfolioItems().get(0);
      assertThat(addedItem.getTitle()).isEqualTo(title);
      assertThat(addedItem.getDescription()).isEqualTo(description);
      assertThat(addedItem.getMediaId()).isEqualTo(mediaId);
    }
  }

  @Nested
  class ServiceConversion {

    @Test
    void toUserServiceJpa_shouldMapDomainToJpa() {
      // Given
      var serviceProviderId =
          new cm.klg.service_provider.domain.service_provider.ServiceProviderId(UUID.randomUUID());
      var serviceTypeId = new ServiceTypeId(UUID.randomUUID());
      var yearOfExperience =
          new cm.klg.service_provider.domain.service_provider.YearOfExperience(5);
      var userDocument =
          new cm.klg.service_provider.domain.service_provider.UserDocument(UUID.randomUUID());

      var userService =
          cm.klg.service_provider.domain.service_provider.UserService.of(
              serviceProviderId, serviceTypeId, yearOfExperience, userDocument);

      // When
      UserServiceJpa result = objectUnderTest.toUserServiceJpa(userService);

      // Then
      assertThat(result.getId().getServiceProviderId()).isEqualTo(serviceProviderId.value());
      assertThat(result.getId().getServiceTypeId()).isEqualTo(serviceTypeId.value());
      assertThat(result.getYearOfExperience()).isEqualTo(5);
    }

    @Test
    void toUserServiceJpa_shouldPreserveDocumentReference() {
      // Given
      var documentId = UUID.randomUUID();
      var serviceProviderId =
          new cm.klg.service_provider.domain.service_provider.ServiceProviderId(UUID.randomUUID());
      var serviceTypeId = new ServiceTypeId(UUID.randomUUID());
      var yearOfExperience =
          new cm.klg.service_provider.domain.service_provider.YearOfExperience(3);
      var userDocument =
          new cm.klg.service_provider.domain.service_provider.UserDocument(documentId);

      var userService =
          cm.klg.service_provider.domain.service_provider.UserService.of(
              serviceProviderId, serviceTypeId, yearOfExperience, userDocument);

      // When
      UserServiceJpa result = objectUnderTest.toUserServiceJpa(userService);

      // Then
      assertThat(result.getUserDocument()).isEqualTo(documentId);
    }
  }

  // Helper methods
  private PortfolioItemJpa createPortfolioItemJpa() {
    var portfolioItemJpa = new PortfolioItemJpa();
    portfolioItemJpa.setId(UUID.randomUUID());
    portfolioItemJpa.setTitle("Test Title");
    portfolioItemJpa.setDescription("Test Description");
    portfolioItemJpa.setMediaId(UUID.randomUUID());
    portfolioItemJpa.setCreatedAt(LocalDateTime.now());
    return portfolioItemJpa;
  }

  private UserServiceJpa createUserServiceJpa(
      UUID serviceProviderId, UUID serviceTypeId, int yearOfExp) {
    var userServiceJpa = new UserServiceJpa();
    userServiceJpa.setId(userServiceJpaId(serviceProviderId, serviceTypeId));
    userServiceJpa.setYearOfExperience(yearOfExp);
    userServiceJpa.setUserDocument(
        UUID.fromString("00000000-0000-0000-0000-000000000000")); // placeholder
    userServiceJpa.setCreatedAt(LocalDateTime.now());
    return userServiceJpa;
  }

  private cm.klg.service_provider.domain.service_provider.PortfolioItem createPortfolioItem() {
    return cm.klg.service_provider.domain.service_provider.PortfolioItem.of(
        PortfolioItemTitle.from("Test Title"),
        PortfolioItemDescription.from("Test Description"),
        new PortfolioItemMediaId(UUID.randomUUID()));
  }

  private UserServiceJpaId userServiceJpaId(UUID serviceProviderId, UUID serviceTypeId) {
    var id = new UserServiceJpaId();
    id.setServiceProviderId(serviceProviderId);
    id.setServiceTypeId(serviceTypeId);
    return id;
  }
}
