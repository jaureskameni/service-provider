package cm.klg.service_provider.adapter.rest.inbound;

import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreatePortfolioItemRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreateProviderServiceDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.IdentityVerificationStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.MyServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PortfolioItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ProfileImageReviewIdentityVerificationDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ProfileImageReviewPageDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ProfileImageReviewProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ProfileImageReviewQueueItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ProfileImageReviewStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ProfileImageReviewSubmissionDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.RejectedServiceProviderIdentityDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.RejectionReasonDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ResubmitServiceProviderApplicationDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceCatalogItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderProfileImageReviewDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPublicProfileDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderVerificationDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UpdateApprovedServiceProviderProfileDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UserProfileDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UserServiceDTO;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.AddPortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.InitialProviderService;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.ProviderRegistrationProfile;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetMyFavoriteServiceProvidersUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase.Response;
import cm.klg.service_provider.application.usecase.ResubmitRejectedServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdateApprovedServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdatePortfolioItemUseCase;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews.IdentityVerificationView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ProfileImageReviewSummaryView;
import cm.klg.service_provider.application.views.ServiceProviderViews.RejectedServiceProviderIdentityView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderVerificationView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView2;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView3;
import cm.klg.service_provider.application.views.ServiceProviderViews.UserView;
import cm.klg.service_provider.application.views.ServiceTypeViews;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.CniRectoMediaId;
import cm.klg.service_provider.domain.service_provider.CniVersoMediaId;
import cm.klg.service_provider.domain.service_provider.IdentityDocuments;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ProfileImageMediaId;
import cm.klg.service_provider.domain.service_provider.ProviderContact;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ProviderProfile;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestMapper {

  default BecomeServiceProviderCommand toBecomeServiceProviderCommand(
      ServiceProviderRegisterDTO dto, UUID currentUserId) {
    return new BecomeServiceProviderCommand(
        UserId.from(currentUserId),
        new ProviderRegistrationProfile(
            new ProviderLocation(
                UserCityId.from(dto.getCity()),
                UserDistrictId.from(dto.getDistrict()),
                dto.getQuarter() != null ? UserQuarterId.from(dto.getQuarter()) : null),
            PhoneNumber.from(
                dto.getPhoneNumber().getCountryCode(), dto.getPhoneNumber().getNumber()),
            dto.getAbout() != null ? AboutProvider.from(dto.getAbout()) : null,
            new IdentityDocuments(
                CniRectoMediaId.from(dto.getCniRectoId()),
                CniVersoMediaId.from(dto.getCniVersoId())),
            ProfileImageMediaId.from(dto.getProfileImageId())),
        new InitialProviderService(
            new ServiceTypeId(dto.getServiceType().getId()),
            YearOfExperience.from(dto.getServiceType().getYearOfExperience()),
            UserDocument.from(dto.getServiceType().getDocument())));
  }

  default ResubmitRejectedServiceProviderUseCase.Command toResubmitRejectedServiceProviderCommand(
      ResubmitServiceProviderApplicationDTO dto, UUID currentUserId) {
    var identityDocuments =
        dto.getCniRectoId() != null && dto.getCniVersoId() != null
            ? new IdentityDocuments(
                CniRectoMediaId.from(dto.getCniRectoId()),
                CniVersoMediaId.from(dto.getCniVersoId()))
            : null;
    return new ResubmitRejectedServiceProviderUseCase.Command(
        UserId.from(currentUserId),
        toProviderProfile(
            dto.getCity(),
            dto.getDistrict(),
            dto.getQuarter(),
            dto.getAbout(),
            dto.getPhoneNumber()),
        identityDocuments,
        ProfileImageMediaId.from(dto.getProfileImageId()));
  }

  default UpdateApprovedServiceProviderUseCase.Command toUpdateApprovedServiceProviderCommand(
      UpdateApprovedServiceProviderProfileDTO dto, UUID currentUserId) {
    return new UpdateApprovedServiceProviderUseCase.Command(
        UserId.from(currentUserId),
        toProviderProfile(
            dto.getCity(),
            dto.getDistrict(),
            dto.getQuarter(),
            dto.getAbout(),
            dto.getPhoneNumber()),
        ProfileImageMediaId.from(dto.getProfileImageId()));
  }

  private ProviderProfile toProviderProfile(
      UUID city,
      UUID district,
      @Nullable UUID quarter,
      @Nullable String about,
      PhoneNumberDTO phoneNumber) {
    return new ProviderProfile(
        new ProviderContact(
            new ProviderLocation(
                UserCityId.from(city),
                UserDistrictId.from(district),
                quarter != null ? UserQuarterId.from(quarter) : null),
            PhoneNumber.from(phoneNumber.getCountryCode(), phoneNumber.getNumber())),
        about != null ? AboutProvider.from(about) : null);
  }

  default AddNewServiceUseCase.AddNewServiceCommand toAddNewServiceCommand(
      CreateProviderServiceDTO dto, UUID currentUserId) {
    return new AddNewServiceUseCase.AddNewServiceCommand(
        UserId.from(currentUserId),
        new ServiceTypeId(dto.getServiceTypeId()),
        YearOfExperience.from(dto.getYearOfExperience()),
        UserDocument.from(dto.getDocument()));
  }

  ServiceCatalogItemDTO toServiceCatalogItemDTO(ServiceTypeViews.ServiceTypeView serviceTypeView);

  default Map<String, List<ServiceCatalogItemDTO>> toGroupedServiceCatalogDTOs(
      List<ServiceTypeViews.ServiceTypeView> serviceTypeViews) {
    return serviceTypeViews.stream()
        .map(this::toServiceCatalogItemDTO)
        .collect(Collectors.groupingBy(ServiceCatalogItemDTO::getCategory));
  }

  ServiceProviderStatus toServiceProviderStatus(ServiceProviderStatusDTO status);

  default RejectionReason toRejectionReason(String rejectionReason) {
    return RejectionReason.valueOf(rejectionReason);
  }

  default MyServiceProviderDTO toMyServiceProviderDTO(ServiceProviderView2 provider) {
    return new MyServiceProviderDTO()
        .id(provider.id())
        .user(toUserProfileDTO(provider.user()))
        .status(ServiceProviderStatusDTO.fromValue(provider.status()))
        .rejectionReason(
            provider.rejectionReason() != null
                ? RejectionReasonDTO.fromValue(provider.rejectionReason())
                : null)
        .canEdit(!ServiceProviderStatus.PENDING.name().equals(provider.status()))
        .city(provider.cityId())
        .district(provider.districtId())
        .quarter(provider.quarterId())
        .about(provider.about())
        .phoneNumber(toPhoneNumberDTO(provider.phoneNumber()))
        .profileImageId(provider.profileImageId())
        .pendingProfileImageId(provider.pendingProfileImageId())
        .profileImageReviewStatus(
            ProfileImageReviewStatusDTO.fromValue(provider.profileImageReviewStatus().name()))
        .identityVerificationStatus(
            IdentityVerificationStatusDTO.fromValue(provider.identityVerificationStatus().name()))
        .userServices(toUserServiceDTOs(provider.services()));
  }

  default RejectedServiceProviderIdentityDTO toRejectedServiceProviderIdentityDTO(
      RejectedServiceProviderIdentityView identity) {
    return new RejectedServiceProviderIdentityDTO()
        .providerId(identity.providerId())
        .cniRectoId(identity.cniRectoId())
        .cniVersoId(identity.cniVersoId())
        .profileImageId(identity.profileImageId())
        .status(RejectedServiceProviderIdentityDTO.StatusEnum.REJECTED)
        .rejectionReason(RejectionReasonDTO.fromValue(identity.rejectionReason().name()));
  }

  default ServiceProviderVerificationDTO toServiceProviderVerificationDTO(
      ServiceProviderVerificationView provider) {
    return new ServiceProviderVerificationDTO()
        .id(provider.id())
        .userId(provider.userId())
        .user(toUserProfileDTO(provider.user()))
        .city(provider.cityId())
        .district(provider.districtId())
        .quarter(provider.quarterId())
        .approvedBy(provider.approvedBy())
        .rejectedBy(provider.rejectedBy())
        .rejectionReason(provider.rejectionReason())
        .about(provider.about())
        .phoneNumber(toPhoneNumberDTO(provider.phoneNumber()))
        .status(ServiceProviderStatusDTO.fromValue(provider.status()))
        .profileImageId(provider.profileImageId())
        .pendingProfileImageId(provider.pendingProfileImageId())
        .profileImageReviewStatus(
            ProfileImageReviewStatusDTO.fromValue(provider.profileImageReviewStatus().name()))
        .identityVerification(
            provider.identityVerification() != null
                ? toProfileImageReviewIdentityVerificationDTO(provider.identityVerification())
                : null)
        .createdAt(provider.createdAt())
        .updatedAt(provider.updatedAt())
        .services(toUserServiceDTOs(provider.services()))
        .portfolios(provider.portfolios().stream().map(this::toPortfolioItemDTO).toList());
  }

  default ProfileImageReviewPageDTO toProfileImageReviewPageDTO(
      List<ProfileImageReviewSummaryView> providers, long totalElements) {
    return new ProfileImageReviewPageDTO()
        .totalElements(totalElements)
        .serviceProviders(providers.stream().map(this::toProfileImageReviewQueueItemDTO).toList());
  }

  default UserProfileDTO toUserProfileDTO(UserView user) {
    return toUserProfileDTO(user, true);
  }

  default UserProfileDTO toUserProfileDTO(UserView user, boolean includeContactEmail) {
    return new UserProfileDTO()
        .id(user.id())
        .firstname(user.firstname())
        .lastname(user.lastname())
        .email(includeContactEmail ? user.email() : null)
        .createdAt(user.createdAt());
  }

  default ProfileImageReviewQueueItemDTO toProfileImageReviewQueueItemDTO(
      ProfileImageReviewSummaryView provider) {
    return new ProfileImageReviewQueueItemDTO()
        .serviceProviderId(provider.serviceProviderId())
        .userId(provider.userId())
        .user(toUserProfileDTO(provider.user()))
        .phoneNumber(toPhoneNumberDTO(provider.phoneNumber()))
        .city(provider.cityId())
        .district(provider.districtId())
        .quarter(provider.quarterId())
        .status(ServiceProviderStatusDTO.fromValue(provider.status().name()))
        .currentProfileImageId(provider.currentProfileImageId())
        .pendingProfileImageId(provider.pendingProfileImageId())
        .profileImageReviewStatus(
            ProfileImageReviewStatusDTO.fromValue(provider.profileImageReviewStatus().name()))
        .updatedAt(provider.submittedAt());
  }

  default ServiceProviderProfileImageReviewDTO toServiceProviderProfileImageReviewDTO(
      ServiceProviderView3 review) {
    return new ServiceProviderProfileImageReviewDTO()
        .serviceProvider(
            new ProfileImageReviewProviderDTO()
                .id(review.id())
                .userId(review.userId())
                .user(toUserProfileDTO(review.user()))
                .phoneNumber(toPhoneNumberDTO(review.phoneNumber()))
                .city(review.cityId())
                .district(review.districtId())
                .quarter(review.quarterId())
                .about(review.about())
                .status(ServiceProviderStatusDTO.fromValue(review.status().name()))
                .currentProfileImageId(review.profileImageId())
                .userServices(toUserServiceDTOs(review.services()))
                .createdAt(review.createdAt())
                .updatedAt(review.updatedAt()))
        .profileImageReview(
            new ProfileImageReviewSubmissionDTO()
                .status(
                    ProfileImageReviewStatusDTO.fromValue(review.profileImageReviewStatus().name()))
                .pendingProfileImageId(review.pendingProfileImageId())
                .rejectionReason(
                    review.profileImageRejectionReason() != null
                        ? RejectionReasonDTO.fromValue(review.profileImageRejectionReason().name())
                        : null)
                .submittedAt(review.submittedAt()))
        .identityVerification(
            toProfileImageReviewIdentityVerificationDTO(review.identityVerification()));
  }

  default ProfileImageReviewIdentityVerificationDTO toProfileImageReviewIdentityVerificationDTO(
      IdentityVerificationView verification) {
    return new ProfileImageReviewIdentityVerificationDTO()
        .status(IdentityVerificationStatusDTO.fromValue(verification.status().name()))
        .cniRectoId(verification.cniRectoId())
        .cniVersoId(verification.cniVersoId())
        .rejectionReason(
            verification.rejectionReason() != null
                ? RejectionReasonDTO.fromValue(verification.rejectionReason().name())
                : null)
        .verifiedAt(verification.verifiedAt())
        .verifiedBy(verification.verifiedBy());
  }

  default GetAllServiceProviderUseCase.Command toGetAllServiceProviderCommand(
      Integer limit, ServiceProviderStatusDTO status, Integer page) {
    return new GetAllServiceProviderUseCase.Command(
        toServiceProviderStatus(status),
        Optional.ofNullable(limit).orElse(20),
        Optional.ofNullable(page).orElse(0));
  }

  default SearchServiceProviderUseCase.Command toSearchServiceProviderCommand(
      UUID serviceTypeId,
      UUID cityId,
      UUID districtId,
      UUID quarterId,
      Integer page,
      Integer limit) {
    return new SearchServiceProviderUseCase.Command(
        new ServiceTypeId(serviceTypeId),
        new UserCityId(cityId),
        districtId != null ? new UserDistrictId(districtId) : null,
        quarterId != null ? new UserQuarterId(quarterId) : null,
        ServiceProviderStatus.APPROVED, // Default search status
        Optional.ofNullable(limit).orElse(20),
        Optional.ofNullable(page).orElse(0));
  }

  default ServiceProviderPaginateDTO toServiceProviderPaginateDTO(
      GetAllServiceProviderUseCase.Response pageData) {

    return new ServiceProviderPaginateDTO()
        .count(pageData.count())
        .serviceProvider(
            pageData.serviceProviderViews().stream()
                .map(provider -> toServiceProviderDTO(provider, true))
                .toList());
  }

  default ServiceProviderPaginateDTO toServiceProviderPaginateDTO(
      SearchServiceProviderUseCase.Response pageData) {

    return new ServiceProviderPaginateDTO()
        .count(pageData.count())
        .serviceProvider(
            pageData.serviceProviderViews().stream().map(this::toServiceProviderDTO).toList());
  }

  default ServiceProviderPaginateDTO toServiceProviderPaginateDTO(
      GetMyFavoriteServiceProvidersUseCase.Response pageData) {

    return new ServiceProviderPaginateDTO()
        .count(pageData.count())
        .serviceProvider(
            pageData.serviceProviderViews().stream().map(this::toServiceProviderDTO).toList());
  }

  default GetMyFavoriteServiceProvidersUseCase.Command toGetMyFavoriteServiceProvidersCommand(
      UUID userId, Integer limit, Integer page) {
    return new GetMyFavoriteServiceProvidersUseCase.Command(
        UserId.from(userId),
        Optional.ofNullable(limit).orElse(20),
        Optional.ofNullable(page).orElse(0));
  }

  default ServiceProviderDTO toServiceProviderDTO(ServiceProviderView1 v) {
    return toServiceProviderDTO(v, false);
  }

  default ServiceProviderDTO toServiceProviderDTO(
      ServiceProviderView1 v, boolean includeContactInfo) {
    return toServiceProviderDTOFromCommon(
        new UserInfo(v.id(), v.userId(), v.user()),
        new ProviderLocation(
            UserCityId.from(v.cityId()),
            UserDistrictId.from(v.districtId()),
            v.quarterId() != null ? UserQuarterId.from(v.quarterId()) : null),
        new ProviderMetadata(
            v.about(), v.phoneNumber(), v.createdAt(), v.updatedAt(), v.profileImageId()),
        includeContactInfo);
  }

  private ServiceProviderDTO toServiceProviderDTOFromCommon(
      UserInfo userInfo,
      ProviderLocation location,
      ProviderMetadata metadata,
      boolean includeContactInfo) {
    return new ServiceProviderDTO()
        .id(userInfo.id())
        .userId(userInfo.userId())
        .user(toUserProfileDTO(userInfo.user(), includeContactInfo))
        .city(location.cityId().value())
        .district(location.districtId().value())
        .quarter(location.quarterId() != null ? location.quarterId().value() : null)
        .about(metadata.about() != null ? metadata.about() : null)
        .createdAt(metadata.createdAt())
        .updatedAt(metadata.updatedAt())
        .phoneNumber(includeContactInfo ? toPhoneNumberDTO(metadata.phoneNumber()) : null)
        .profileImageId(metadata.profileImageId());
  }

  @Nullable
  default PhoneNumberDTO toPhoneNumberDTO(@Nullable PhoneNumber phoneNumber) {

    if (phoneNumber == null) {
      return null;
    }
    return new PhoneNumberDTO().countryCode(phoneNumber.countryCode()).number(phoneNumber.number());
  }

  default UserServiceDTO toUserServiceDTO(UserServiceView userServiceView) {
    return new UserServiceDTO()
        .serviceName(userServiceView.serviceType().name())
        .serviceCategory(userServiceView.serviceType().category())
        .yearOfExperience(userServiceView.yearOfExperience())
        .document(userServiceView.document())
        .createdAt(userServiceView.createdAt());
  }

  default AddPortfolioItemUseCase.Command toAddPortfolioItemCommand(
      CreatePortfolioItemRequestDTO dto, UUID userId) {
    return new AddPortfolioItemUseCase.Command(
        UserId.from(userId),
        PortfolioItemTitle.from(dto.getTitle()),
        PortfolioItemDescription.from(dto.getDescription()),
        PortfolioItemMediaId.from(dto.getMediaId()));
  }

  default UpdatePortfolioItemUseCase.Command toUpdatePortfolioItemCommand(
      UUID portfolioItemId, CreatePortfolioItemRequestDTO dto, UUID userId) {
    return new UpdatePortfolioItemUseCase.Command(
        UserId.from(userId),
        PortfolioItemId.from(portfolioItemId),
        PortfolioItemTitle.from(dto.getTitle()),
        PortfolioItemDescription.from(dto.getDescription()),
        PortfolioItemMediaId.from(dto.getMediaId()));
  }

  default PortfolioItemDTO toPortfolioItemDTO(PortfolioView portfolioView) {
    return new PortfolioItemDTO()
        .id(portfolioView.id())
        .title(portfolioView.title())
        .description(portfolioView.description())
        .mediaId(portfolioView.mediaId())
        .createdAt(portfolioView.createdAt());
  }

  default List<PortfolioItemDTO> toPortfolioItemDTOs(List<PortfolioView> portfolioViews) {
    return portfolioViews.stream().map(this::toPortfolioItemDTO).toList();
  }

  default List<UserServiceDTO> toUserServiceDTOs(List<UserServiceView> userServiceViews) {
    return userServiceViews.stream().map(this::toUserServiceDTO).toList();
  }

  default GetServiceProviderByIdUseCase.Command toGetServiceProviderByIdCommand(
      UUID serviceProviderId) {
    return new GetServiceProviderByIdUseCase.Command(
        ServiceProviderId.from(serviceProviderId), null);
  }

  default ServiceProviderPublicProfileDTO toServiceProviderPublicProfileDTO(Response result) {
    var v = result.serviceProviderView2();
    return new ServiceProviderPublicProfileDTO()
        .id(v.id())
        .userId(v.userId())
        .user(toUserProfileDTO(v.user(), false))
        .city(v.cityId())
        .district(v.districtId())
        .quarter(v.quarterId())
        .about(v.about())
        .createdAt(v.createdAt())
        .updatedAt(v.updatedAt())
        .phoneNumber(null)
        .userIsProviderClient(false)
        .userHasFavorited(result.isFavorite())
        .profileImageId(v.profileImageId())
        .userServices(v.services().stream().map(this::toUserServiceDTO).toList())
        .portfolio(v.portfolios().stream().map(this::toPortfolioItemDTO).toList());
  }

  record ProviderMetadata(
      @Nullable String about,
      PhoneNumber phoneNumber,
      @Nullable LocalDateTime createdAt,
      @Nullable LocalDateTime updatedAt,
      UUID profileImageId) {}

  record UserInfo(UUID id, UUID userId, UserView user) {}
}
