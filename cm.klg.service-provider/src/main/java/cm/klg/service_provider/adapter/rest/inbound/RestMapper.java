package cm.klg.service_provider.adapter.rest.inbound;

import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PhoneNumberDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceCatalogItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UserServiceDTO;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderProfileUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.views.ServiceProviderViews;
import cm.klg.service_provider.application.views.ServiceTypeViews;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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

  ServiceCatalogItemDTO toServiceCatalogItemDTO(ServiceTypeViews.ServiceTypeView serviceTypeView);

  default Map<String, List<ServiceCatalogItemDTO>> toGroupedServiceCatalogDTOs(
      List<ServiceTypeViews.ServiceTypeView> serviceTypeViews) {
    return serviceTypeViews.stream()
        .map(this::toServiceCatalogItemDTO)
        .collect(Collectors.groupingBy(ServiceCatalogItemDTO::getCategory));
  }

  ServiceProviderStatus toServiceProviderStatus(ServiceProviderStatusDTO status);

  default GetAllServiceProviderUseCase.Command toGetAllServiceProviderCommand(
      Integer limit, ServiceProviderStatusDTO status, Integer page) {
    return new GetAllServiceProviderUseCase.Command(
        toServiceProviderStatus(status),
        java.util.Optional.ofNullable(limit).orElse(20),
        java.util.Optional.ofNullable(page).orElse(0));
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
        java.util.Optional.ofNullable(limit).orElse(20),
        java.util.Optional.ofNullable(page).orElse(0));
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

  default ServiceProviderDTO toServiceProviderProfileDTO(
      GetServiceProviderProfileUseCase.Response response) {
    var dto = toServiceProviderDTO(response.profile());
    if (!response.isClient()) {
      dto.setPhoneNumber(null);
    }
    return dto;
  }

  default ServiceProviderDTO toServiceProviderDTO(
      ServiceProviderViews.ServiceProviderView serviceProviderView) {

    return new ServiceProviderDTO()
        .id(serviceProviderView.id())
        .userId(serviceProviderView.userId())
        .firstname(serviceProviderView.firstname())
        .lastname(serviceProviderView.lastname())
        .city(serviceProviderView.cityId())
        .district(serviceProviderView.districtId())
        .quarter(serviceProviderView.quarterId())
        .createdAt(serviceProviderView.createdAt())
        .updatedAt(serviceProviderView.updatedAt())
        .phoneNumber(toPhoneNumberDTO(serviceProviderView.phoneNumber()))
        .serviceProviderStatus(ServiceProviderStatusDTO.fromValue(serviceProviderView.status()))
        .userServices(serviceProviderView.services().stream().map(this::toUserServiceDTO).toList());
  }

  default PhoneNumberDTO toPhoneNumberDTO(PhoneNumber phoneNumber) {
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
}
