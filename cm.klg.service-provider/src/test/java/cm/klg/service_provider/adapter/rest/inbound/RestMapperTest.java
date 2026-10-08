package cm.klg.service_provider.adapter.rest.inbound;

import static cm.klg.service_provider.application.views.ServiceProviderViews.*;
import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreatePortfolioItemRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ProfileImageReviewQueueItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceCatalogItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderProfileImageReviewDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderVerificationDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UserServiceDTO;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdatePortfolioItemUseCase;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.IdentityVerificationStatus;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ProfileImageReviewStatus;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestMapperTest {

  private final RestMapper objectUnderTest = new RestMapperImpl();

  @Test
  void serviceProviderVerificationDTO_omitsIdentityWhenItIsNull() throws Exception {
    var dto = new ServiceProviderVerificationDTO().identityVerification(null);

    var json = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(dto));

    assertThat(json.has("identityVerification")).isFalse();
  }

  @Test
  void toServiceProviderProfileImageReviewDTO_includesPrivateDocumentsInDedicatedAdminView() {
    var frontId = UUID.randomUUID();
    var backId = UUID.randomUUID();
    var now = LocalDateTime.now();
    var verification =
        new IdentityVerificationView(
            UUID.randomUUID(),
            frontId,
            backId,
            IdentityVerificationStatus.APPROVED,
            null,
            null,
            null);

    var review =
        new ServiceProviderView3(
            UUID.randomUUID(),
            UUID.randomUUID(),
            new UserView(UUID.randomUUID(), "Ari", "Nana", null, now),
            PhoneNumber.from("+237", "678901234"),
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            null,
            ServiceProviderStatus.APPROVED,
            UUID.randomUUID(),
            UUID.randomUUID(),
            ProfileImageReviewStatus.PENDING_REVIEW,
            null,
            now,
            verification,
            now,
            null,
            List.of());
    ServiceProviderProfileImageReviewDTO result =
        objectUnderTest.toServiceProviderProfileImageReviewDTO(review);

    assertThat(result.getServiceProvider().getUser().getFirstname()).isEqualTo("Ari");
    assertThat(result.getServiceProvider().getCurrentProfileImageId()).isNotNull();
    assertThat(result.getProfileImageReview().getPendingProfileImageId()).isNotNull();
    assertThat(result.getIdentityVerification().getCniRectoId()).isEqualTo(frontId);
    assertThat(result.getIdentityVerification().getCniVersoId()).isEqualTo(backId);

    var summary =
        new ProfileImageReviewSummaryView(
            UUID.randomUUID(),
            UUID.randomUUID(),
            new UserView(UUID.randomUUID(), "Ari", "Nana", null, now),
            PhoneNumber.from("+237", "678901234"),
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            ServiceProviderStatus.APPROVED,
            UUID.randomUUID(),
            UUID.randomUUID(),
            ProfileImageReviewStatus.PENDING_REVIEW,
            now);
    ProfileImageReviewQueueItemDTO queueItem =
        objectUnderTest.toProfileImageReviewQueueItemDTO(summary);
    assertThat(queueItem.getServiceProviderId()).isEqualTo(summary.serviceProviderId());
    assertThat(queueItem.getCurrentProfileImageId()).isEqualTo(summary.currentProfileImageId());
    assertThat(queueItem.getPendingProfileImageId()).isEqualTo(summary.pendingProfileImageId());
  }

  @Test
  void toBecomeServiceProviderCommand_shouldMapAllFieldsCorrectly() {
    // Given
    UUID currentUserId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID quarterId = UUID.randomUUID();
    String countryCode = "+237";
    String phoneNumber = "678901234";
    UUID serviceTypeId = UUID.randomUUID();
    int yearOfExperience = 5;
    UUID documentId = UUID.randomUUID();

    ServiceProviderRegisterDTO serviceProviderRegisterDTO =
        new ServiceProviderRegisterDTO()
            .city(cityId)
            .district(districtId)
            .quarter(quarterId)
            .phoneNumber(new PhoneNumberDTO().countryCode(countryCode).number(phoneNumber))
            .serviceType(
                new ServiceTypeDTO()
                    .id(serviceTypeId)
                    .yearOfExperience(yearOfExperience)
                    .document(documentId))
            .cniRectoId(UUID.randomUUID())
            .cniVersoId(UUID.randomUUID())
            .profileImageId(UUID.randomUUID());

    // When
    BecomeServiceProviderCommand command =
        objectUnderTest.toBecomeServiceProviderCommand(serviceProviderRegisterDTO, currentUserId);

    // Then
    assertThat(command).isNotNull();
    assertThat(command.userId().value()).isEqualTo(currentUserId);
    assertThat(command.profile().location().cityId().value()).isEqualTo(cityId);
    assertThat(command.profile().location().districtId().value()).isEqualTo(districtId);
    assertThat(command.profile().location().quarterId().value()).isEqualTo(quarterId);
    assertThat(command.profile().phoneNumber().countryCode()).isEqualTo(countryCode);
    assertThat(command.profile().phoneNumber().number()).isEqualTo(phoneNumber);
    assertThat(command.profile().profileImageId().value()).isNotNull();
    assertThat(command.initialService().serviceTypeId().value()).isEqualTo(serviceTypeId);
    assertThat(command.initialService().yearOfExperience().value()).isEqualTo(yearOfExperience);
    assertThat(command.initialService().document().value()).isEqualTo(documentId);
  }

  @Test
  void toSearchServiceProviderCommand_shouldMapCorrectly() {
    // Given
    UUID serviceTypeId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID quarterId = UUID.randomUUID();

    // When
    SearchServiceProviderUseCase.Command command =
        objectUnderTest.toSearchServiceProviderCommand(
            serviceTypeId, cityId, districtId, quarterId, 1, 10);

    // Then
    assertThat(command.serviceTypeId().value()).isEqualTo(serviceTypeId);
    assertThat(command.cityId().value()).isEqualTo(cityId);
    assertThat(command.districtId().value()).isEqualTo(districtId);
    assertThat(command.quarterId().value()).isEqualTo(quarterId);
    assertThat(command.status()).isEqualTo(ServiceProviderStatus.APPROVED);
    assertThat(command.limit()).isEqualTo(10);
  }

  @Test
  void toGroupedServiceCatalogDTOs_shouldGroupByCategory() {
    // Given
    ServiceTypeView view1 = new ServiceTypeView(UUID.randomUUID(), "Plumber", "MAINTENANCE", true);
    ServiceTypeView view2 =
        new ServiceTypeView(UUID.randomUUID(), "Electrician", "MAINTENANCE", true);
    ServiceTypeView view3 = new ServiceTypeView(UUID.randomUUID(), "Nanny", "HOME_SERVICES", true);

    // When
    Map<String, List<ServiceCatalogItemDTO>> result =
        objectUnderTest.toGroupedServiceCatalogDTOs(List.of(view1, view2, view3));

    // Then
    assertThat(result).hasSize(2);
    assertThat(result.get("MAINTENANCE")).hasSize(2);
    assertThat(result.get("HOME_SERVICES")).hasSize(1);
  }

  @Test
  void toPublicServiceProviderProfileDTO_shouldNotIncludePhoneNumber() {
    // Given
    var profile =
        new ServiceProviderView1(
            UUID.randomUUID(),
            UUID.randomUUID(),
            new UserView(UUID.randomUUID(), "John", "Doe", "john@example.com", LocalDateTime.now()),
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            null,
            null,
            null,
            null,
            PhoneNumber.from("+237", "678901234"),
            "APPROVED",
            null,
            LocalDateTime.now(),
            null);

    // When
    var result = objectUnderTest.toServiceProviderDTO(profile);

    // Then
    assertThat(result.getPhoneNumber()).isNull();
  }

  @Test
  void toPortfolioItemDTO_shouldMapAllFields() {
    var id = UUID.randomUUID();
    var mediaId = UUID.randomUUID();
    var now = LocalDateTime.now();
    var view = new PortfolioView(id, "Title", "Description", mediaId, now);

    var result = objectUnderTest.toPortfolioItemDTO(view);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getTitle()).isEqualTo("Title");
    assertThat(result.getDescription()).isEqualTo("Description");
    assertThat(result.getMediaId()).isEqualTo(mediaId);
    assertThat(result.getCreatedAt()).isEqualTo(now);
  }

  @Test
  void toPortfolioItemDTOs_shouldMapList() {
    var view1 =
        new PortfolioView(UUID.randomUUID(), "A", "Desc A", UUID.randomUUID(), LocalDateTime.now());
    var view2 =
        new PortfolioView(UUID.randomUUID(), "B", "Desc B", UUID.randomUUID(), LocalDateTime.now());

    var result = objectUnderTest.toPortfolioItemDTOs(List.of(view1, view2));

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTitle()).isEqualTo("A");
    assertThat(result.get(1).getTitle()).isEqualTo("B");
  }

  @Test
  void toPortfolioItemDTOs_shouldReturnEmptyList_whenNoViews() {
    var result = objectUnderTest.toPortfolioItemDTOs(List.of());
    assertThat(result).isEmpty();
  }

  @Test
  void toUpdatePortfolioItemCommand_shouldMapAllFields() {
    var portfolioId = UUID.randomUUID();
    var userId = UUID.randomUUID();
    var mediaId = UUID.randomUUID();
    var request =
        new CreatePortfolioItemRequestDTO()
            .title("Updated title")
            .description("Updated description")
            .mediaId(mediaId);

    UpdatePortfolioItemUseCase.Command result =
        objectUnderTest.toUpdatePortfolioItemCommand(portfolioId, request, userId);

    assertThat(result.userId()).isEqualTo(UserId.from(userId));
    assertThat(result.portfolioItemId()).isEqualTo(PortfolioItemId.from(portfolioId));
    assertThat(result.title()).isEqualTo(PortfolioItemTitle.from("Updated title"));
    assertThat(result.description())
        .isEqualTo(PortfolioItemDescription.from("Updated description"));
    assertThat(result.mediaId()).isEqualTo(PortfolioItemMediaId.from(mediaId));
  }

  @Test
  void toUserServiceDTO_shouldMapAllFields() {
    var serviceType = new ServiceTypeView(UUID.randomUUID(), "Carpentry", "Construction", true);
    var documentId = UUID.randomUUID();
    var createdAt = LocalDateTime.now();
    var view = new UserServiceView(serviceType, 10, documentId, createdAt);

    UserServiceDTO result = objectUnderTest.toUserServiceDTO(view);

    assertThat(result.getServiceName()).isEqualTo("Carpentry");
    assertThat(result.getServiceCategory()).isEqualTo("Construction");
    assertThat(result.getYearOfExperience()).isEqualTo(10);
    assertThat(result.getDocument()).isEqualTo(documentId);
    assertThat(result.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void toUserServiceDTOs_shouldMapAList() {
    var serviceType = new ServiceTypeView(UUID.randomUUID(), "Plumbing", "Repairs", true);
    var views =
        List.of(
            new UserServiceView(serviceType, 5, UUID.randomUUID(), LocalDateTime.now()),
            new UserServiceView(serviceType, 3, UUID.randomUUID(), LocalDateTime.now()));

    var result = objectUnderTest.toUserServiceDTOs(views);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getYearOfExperience()).isEqualTo(5);
    assertThat(result.get(1).getYearOfExperience()).isEqualTo(3);
  }
}
