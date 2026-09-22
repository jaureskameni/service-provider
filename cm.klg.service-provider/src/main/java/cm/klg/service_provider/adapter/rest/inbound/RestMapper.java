package cm.klg.service_provider.adapter.rest.inbound;

import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreatePortfolioItemRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PortfolioItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.RejectionReasonDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceCatalogItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPublicProfileDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UpdateServiceProviderProfileRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UserServiceDTO;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.AddPortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetMyFavoriteServiceProvidersUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase.Response;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdatePortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.UpdateServiceProviderProfileUseCase;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView2;
import cm.klg.service_provider.application.views.ServiceTypeViews;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId.value", source = "currentUserId")
  @Mapping(target = "location.cityId.value", source = "serviceProviderRegisterDTO.city")
  @Mapping(target = "location.districtId.value", source = "serviceProviderRegisterDTO.district")
  @Mapping(target = "location.quarterId.value", source = "serviceProviderRegisterDTO.quarter")
  @Mapping(target = "about.value", source = "serviceProviderRegisterDTO.about")
  @Mapping(target = "document.value", source = "serviceProviderRegisterDTO.serviceType.document")
  @Mapping(target = "serviceTypeId.value", source = "serviceProviderRegisterDTO.serviceType.id")
  @Mapping(
      target = "yearOfExperience.value",
      source = "serviceProviderRegisterDTO.serviceType.yearOfExperience")
  @Mapping(
      target = "phoneNumber.countryCode",
      source = "serviceProviderRegisterDTO.phoneNumber.countryCode")
  @Mapping(target = "phoneNumber.number", source = "serviceProviderRegisterDTO.phoneNumber.number")
  BecomeServiceProviderCommand toBecomeServiceProviderCommand(
      ServiceProviderRegisterDTO serviceProviderRegisterDTO, UUID currentUserId);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId.value", source = "currentUserId")
  @Mapping(target = "serviceTypeId.value", source = "serviceTypeDTO.id")
  @Mapping(target = "yearOfExperience.value", source = "serviceTypeDTO.yearOfExperience")
  @Mapping(target = "userDocument.value", source = "serviceTypeDTO.document")
  AddNewServiceUseCase.AddNewServiceCommand toAddNewServiceCommand(
      ServiceTypeDTO serviceTypeDTO, UUID currentUserId);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId.value", source = "currentUserId")
  @Mapping(target = "location.cityId.value", source = "request.city")
  @Mapping(target = "location.districtId.value", source = "request.district")
  @Mapping(target = "location.quarterId.value", source = "request.quarter")
  @Mapping(target = "phoneNumber.countryCode", source = "request.phoneNumber.countryCode")
  @Mapping(target = "phoneNumber.number", source = "request.phoneNumber.number")
  @Mapping(target = "about.value", source = "request.about")
  UpdateServiceProviderProfileUseCase.Command toUpdateServiceProviderProfileCommand(
      UpdateServiceProviderProfileRequestDTO request, UUID currentUserId);

  ServiceCatalogItemDTO toServiceCatalogItemDTO(ServiceTypeViews.ServiceTypeView serviceTypeView);

  default Map<String, List<ServiceCatalogItemDTO>> toGroupedServiceCatalogDTOs(
      List<ServiceTypeViews.ServiceTypeView> serviceTypeViews) {
    return serviceTypeViews.stream()
        .map(this::toServiceCatalogItemDTO)
        .collect(Collectors.groupingBy(ServiceCatalogItemDTO::getCategory));
  }

  ServiceProviderStatus toServiceProviderStatus(ServiceProviderStatusDTO status);

  default RejectionReason toRejectionReason(RejectionReasonDTO rejectionReasonDTO) {
    return new RejectionReason(rejectionReasonDTO.getReason());
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
            pageData.serviceProviderViews().stream().map(this::toServiceProviderDTO).toList());
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
    return toServiceProviderDTOFromCommon(
        new UserInfo(v.id(), v.userId(), v.firstname(), v.lastname()),
        new ProviderLocation(
            UserCityId.from(v.cityId()),
            UserDistrictId.from(v.districtId()),
            v.quarterId() != null ? UserQuarterId.from(v.quarterId()) : null),
        new ProviderMetadata(v.about(), v.phoneNumber(), v.createdAt(), v.updatedAt()));
  }

  private ServiceProviderDTO toServiceProviderDTOFromCommon(
      UserInfo userInfo, ProviderLocation location, ProviderMetadata metadata) {
    return new ServiceProviderDTO()
        .id(userInfo.id())
        .userId(userInfo.userId())
        .firstname(userInfo.firstname())
        .lastname(userInfo.lastname())
        .city(location.cityId().value())
        .district(location.districtId().value())
        .quarter(location.quarterId() != null ? location.quarterId().value() : null)
        .about(metadata.about() != null ? metadata.about() : null)
        .createdAt(metadata.createdAt())
        .updatedAt(metadata.updatedAt())
        .phoneNumber(toPhoneNumberDTO(metadata.phoneNumber()));
  }

  default ServiceProviderDTO toServiceProviderProfileDTO(ServiceProviderView1 serviceProviderView) {
    return toServiceProviderDTO(serviceProviderView);
  }

  default ServiceProviderDTO toServiceProviderDTO(ServiceProviderView2 v) {
    return toServiceProviderDTOFromCommon(
        new UserInfo(v.id(), v.userId(), v.firstname(), v.lastname()),
        new ProviderLocation(
            UserCityId.from(v.cityId()),
            UserDistrictId.from(v.districtId()),
            v.quarterId() != null ? UserQuarterId.from(v.quarterId()) : null),
        new ProviderMetadata(v.about(), v.phoneNumber(), v.createdAt(), v.updatedAt()));
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
    PhoneNumber phoneNumber = v.phoneNumber();
    if (!result.isClient()) {
      phoneNumber = null;
    }
    return new ServiceProviderPublicProfileDTO()
        .id(v.id())
        .userId(v.userId())
        .firstname(v.firstname())
        .lastname(v.lastname())
        .city(v.cityId())
        .district(v.districtId())
        .quarter(v.quarterId())
        .about(v.about())
        .createdAt(v.createdAt())
        .updatedAt(v.updatedAt())
        .phoneNumber(toPhoneNumberDTO(phoneNumber))
        .userIsProviderClient(result.isClient())
        .userHasFavorited(result.isFavorite())
        .serviceProviderStatus(ServiceProviderStatusDTO.fromValue(v.status()))
        .userServices(v.services().stream().map(this::toUserServiceDTO).toList())
        .portfolio(v.portfolios().stream().map(this::toPortfolioItemDTO).toList());
  }

  record ProviderMetadata(
      @Nullable String about,
      PhoneNumber phoneNumber,
      @Nullable LocalDateTime createdAt,
      @Nullable LocalDateTime updatedAt) {}

  record UserInfo(UUID id, UUID userId, String firstname, String lastname) {}
}
