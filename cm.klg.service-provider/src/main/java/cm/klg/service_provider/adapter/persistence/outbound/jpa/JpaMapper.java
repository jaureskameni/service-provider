package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.IdentityId;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.provider_client.ProviderClient;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.ProviderAudit;
import cm.klg.service_provider.domain.service_provider.ProviderContact;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ProviderReview;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.UserService;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceType;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JpaMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "identityId", source = "identityId.value")
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "emailAddress", source = "email.value")
  @Mapping(target = "phoneNumber", source = "phoneNumber")
  @Mapping(target = "serviceProvider", source = "serviceProvider")
  @Mapping(target = "createdAt", source = "createdAt.value")
  UserJpa toUserJpa(User user);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "city", source = "location.cityId.value")
  @Mapping(target = "district", source = "location.districtId.value")
  @Mapping(target = "quarter", source = "location.quarterId.value")
  @Mapping(target = "approvedBy", source = "approvedBy.value")
  @Mapping(target = "rejectedBy", source = "rejectedBy.value")
  @Mapping(target = "rejectionReason", source = "rejectionReason.value")
  @Mapping(target = "about", source = "about.value")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  ServiceProviderJpa fromServiceProviderDomain(ServiceProvider serviceProvider);

  default ServiceProviderJpa toServiceProviderJpa(ServiceProvider serviceProvider) {
    ServiceProviderJpa target = fromServiceProviderDomain(serviceProvider);
    mapUserServicesForInsert(serviceProvider, target);
    return target;
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id.serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "id.serviceTypeId", source = "serviceTypeId.value")
  @Mapping(target = "userDocument", source = "userDocument.value")
  @Mapping(target = "yearOfExperience", source = "yearOfExperience.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  UserServiceJpa toUserServiceJpa(UserService userService);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "name", source = "name.value")
  @Mapping(target = "category", source = "category")
  @Mapping(target = "active", source = "active")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "updatedAt", source = "updated.value")
  ServiceTypeJpa toServiceTypeJpa(ServiceType serviceType);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "providerId", source = "providerId.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  ProviderClientJpa toJpa(ProviderClient providerClient);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "identityId", source = "identityId.value")
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "emailAddress", source = "email.value")
  @Mapping(target = "phoneNumber", source = "phoneNumber")
  @Mapping(target = "serviceProvider", source = "serviceProvider")
  @Mapping(target = "createdAt", source = "createdAt.value")
  void toUserJpa(User user, @MappingTarget UserJpa userJpa);

  default User toUserDomain(UserJpa userJpa) {
    PhoneNumber phoneNumber =
        PhoneNumber.from(
            userJpa.getPhoneNumber().getCountryCode(), userJpa.getPhoneNumber().getNumber());
    UserProfile userProfile =
        new UserProfile(
            Firstname.from(userJpa.getFirstname()),
            Lastname.from(userJpa.getLastname()),
            EmailAddress.from(userJpa.getEmailAddress()),
            phoneNumber);
    return User.reconstitute(
        new UserId(userJpa.getId()),
        new IdentityId(userJpa.getIdentityId()),
        userProfile,
        userJpa.isServiceProvider(),
        CreatedAt.from(userJpa.getCreatedAt()));
  }

  default void mapUserServicesForInsert(
      ServiceProvider serviceProvider, @MappingTarget ServiceProviderJpa target) {
    serviceProvider.getUserServices().forEach(userService -> addUserService(target, userService));
  }

  default void fromUserServicesDomain(
      ServiceProvider serviceProvider, @MappingTarget ServiceProviderJpa target) {
    serviceProvider.getUserServices().stream()
        .filter(
            userService ->
                target.getUserServices().stream()
                    .noneMatch(
                        userServiceJpa ->
                            userService
                                .getServiceTypeId()
                                .value()
                                .equals(userServiceJpa.getId().getServiceTypeId())))
        .forEach(userService -> addUserService(target, userService));
  }

  private void addUserService(ServiceProviderJpa target, UserService userService) {
    UserServiceJpa userServiceJpa = toUserServiceJpa(userService);
    userServiceJpa.setServiceProvider(target);
    target.getUserServices().add(userServiceJpa);
  }

  default ServiceProvider toServiceProviderDomain(ServiceProviderJpa serviceProviderJpa) {
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            new ServiceProviderId(serviceProviderJpa.getId()),
            new UserId(serviceProviderJpa.getUserId()),
            new ProviderContact(
                new ProviderLocation(
                    new UserCityId(serviceProviderJpa.getCity()),
                    new UserDistrictId(serviceProviderJpa.getDistrict()),
                    new UserQuarterId(serviceProviderJpa.getQuarter())),
                new PhoneNumber(
                    serviceProviderJpa.getPhoneNumber().getCountryCode(),
                    serviceProviderJpa.getPhoneNumber().getNumber())),
            new ProviderReview(
                ServiceProviderStatus.valueOf(serviceProviderJpa.getStatus()),
                serviceProviderJpa.getApprovedBy() != null
                    ? new UserId(serviceProviderJpa.getApprovedBy())
                    : null,
                serviceProviderJpa.getRejectedBy() != null
                    ? new UserId(serviceProviderJpa.getRejectedBy())
                    : null,
                serviceProviderJpa.getRejectionReason() != null
                    ? new RejectionReason(serviceProviderJpa.getRejectionReason())
                    : null),
            new ProviderAudit(
                new CreatedAt(serviceProviderJpa.getCreatedAt()),
                serviceProviderJpa.getUpdatedAt() != null
                    ? new CreatedAt(serviceProviderJpa.getUpdatedAt())
                    : null),
            serviceProviderJpa.getAbout() != null
                ? new AboutProvider(serviceProviderJpa.getAbout())
                : null,
            new ArrayList<>());
    serviceProvider.addAllUserService(
        this.toUserServiceDomain(serviceProviderJpa.getUserServices()));
    return serviceProvider;
  }

  default List<UserService> toUserServiceDomain(List<UserServiceJpa> userServices) {
    return userServices.stream()
        .map(
            userServiceJpa ->
                UserService.reconstitute(
                    new ServiceProviderId(userServiceJpa.getId().getServiceProviderId()),
                    new ServiceTypeId(userServiceJpa.getId().getServiceTypeId()),
                    new YearOfExperience(userServiceJpa.getYearOfExperience()),
                    new UserDocument(userServiceJpa.getUserDocument()),
                    new CreatedAt(userServiceJpa.getCreatedAt())))
        .toList();
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "city", source = "location.cityId.value")
  @Mapping(target = "district", source = "location.districtId.value")
  @Mapping(target = "quarter", source = "location.quarterId.value")
  @Mapping(target = "approvedBy", source = "approvedBy.value")
  @Mapping(target = "rejectedBy", source = "rejectedBy.value")
  @Mapping(target = "rejectionReason", source = "rejectionReason.value")
  @Mapping(target = "about", source = "about.value")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  void toServiceProviderJpa(
      @MappingTarget ServiceProviderJpa serviceProviderJpa, ServiceProvider serviceProvider);

  default void fromServiceProvider(
      @MappingTarget ServiceProviderJpa serviceProviderJpa, ServiceProvider serviceProvider) {
    toServiceProviderJpa(serviceProviderJpa, serviceProvider);
    fromUserServicesDomain(serviceProvider, serviceProviderJpa);
  }

  default ServiceProviderView toServiceProviderView(
      ServiceProviderJpa serviceProviderJpa, UserJpa userJpa, List<ServiceTypeJpa> serviceTypes) {
    Map<UUID, ServiceTypeJpa> serviceTypesById =
        serviceTypes.stream().collect(Collectors.toMap(ServiceTypeJpa::getId, Function.identity()));

    List<UserServiceView> services =
        serviceProviderJpa.getUserServices().stream()
            .map(
                us ->
                    new UserServiceView(
                        toServiceTypeView(serviceTypesById.get(us.getId().getServiceTypeId())),
                        us.getYearOfExperience(),
                        us.getUserDocument(),
                        us.getCreatedAt()))
            .toList();

    return new ServiceProviderView(
        serviceProviderJpa.getId(),
        serviceProviderJpa.getUserId(),
        userJpa.getFirstname(),
        userJpa.getLastname(),
        serviceProviderJpa.getCity(),
        serviceProviderJpa.getDistrict(),
        serviceProviderJpa.getQuarter(),
        serviceProviderJpa.getApprovedBy(),
        serviceProviderJpa.getRejectedBy(),
        serviceProviderJpa.getRejectionReason(),
        serviceProviderJpa.getAbout(),
        PhoneNumber.from(
            serviceProviderJpa.getPhoneNumber().getCountryCode(),
            serviceProviderJpa.getPhoneNumber().getNumber()),
        serviceProviderJpa.getStatus(),
        serviceProviderJpa.getCreatedAt(),
        serviceProviderJpa.getUpdatedAt(),
        services);
  }

  default ServiceTypeView toServiceTypeView(ServiceTypeJpa serviceTypeJpa) {
    return new ServiceTypeView(
        serviceTypeJpa.getId(),
        serviceTypeJpa.getName(),
        serviceTypeJpa.getCategory(),
        serviceTypeJpa.isActive());
  }
}
