package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews;
import cm.klg.service_provider.application.views.ServiceProviderViews.IdentityVerificationView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderIdentityView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderVerificationView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView2;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.favorite.FavoriteProvider;
import cm.klg.service_provider.domain.provider_client.ProviderClient;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.CniRectoMediaId;
import cm.klg.service_provider.domain.service_provider.CniVersoMediaId;
import cm.klg.service_provider.domain.service_provider.IdentityVerification;
import cm.klg.service_provider.domain.service_provider.IdentityVerificationDocuments;
import cm.klg.service_provider.domain.service_provider.IdentityVerificationId;
import cm.klg.service_provider.domain.service_provider.IdentityVerificationReview;
import cm.klg.service_provider.domain.service_provider.IdentityVerificationStatus;
import cm.klg.service_provider.domain.service_provider.PortfolioItem;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ProfileImageMediaId;
import cm.klg.service_provider.domain.service_provider.ProfileImageReviewStatus;
import cm.klg.service_provider.domain.service_provider.ProviderAudit;
import cm.klg.service_provider.domain.service_provider.ProviderContact;
import cm.klg.service_provider.domain.service_provider.ProviderImages;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ProviderProfile;
import cm.klg.service_provider.domain.service_provider.ProviderReview;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceCollections;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderState;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.UserService;
import cm.klg.service_provider.domain.service_provider.VerifiedAt;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceType;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "emailAddress", source = "email.value")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
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
  @Mapping(target = "rejectionReason", source = "identityVerification.rejectionReason")
  @Mapping(target = "about", source = "about.value")
  @Mapping(target = "profileImageId", source = "profileImageId.value")
  @Mapping(target = "pendingProfileImageId", source = "pendingProfileImageId.value")
  @Mapping(target = "profileImageReviewStatus", source = "profileImageReviewStatus")
  @Mapping(target = "profileImageRejectionReason", source = "profileImageRejectionReason")
  @Mapping(target = "identityVerification", source = "identityVerification")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  ServiceProviderJpa fromServiceProviderDomain(ServiceProvider serviceProvider);

  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "cniRectoId", source = "documents.cniRectoId.value")
  @Mapping(target = "cniVersoId", source = "documents.cniVersoId.value")
  @Mapping(target = "rejectionReason", source = "rejectionReason")
  @Mapping(target = "verifiedAt", source = "verifiedAt.value")
  @Mapping(target = "verifiedBy", source = "verifiedBy.value")
  IdentityVerificationJpa toIdentityVerificationJpa(IdentityVerification source);

  default ServiceProviderJpa toServiceProviderJpa(ServiceProvider serviceProvider) {
    ServiceProviderJpa target = fromServiceProviderDomain(serviceProvider);
    if (target.getIdentityVerification() != null) {
      target.getIdentityVerification().setServiceProvider(target);
    }
    mapUserServicesForInsert(serviceProvider, target);
    mapPortfolioItemsForInsert(serviceProvider, target);
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
  @Mapping(target = "title", source = "title.value")
  @Mapping(target = "description", source = "description.value")
  @Mapping(target = "mediaId", source = "mediaId.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  PortfolioItemJpa toPortfolioItemJpa(PortfolioItem portfolioItem);

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
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "providerId", source = "providerId.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  FavoriteProviderJpa toJpa(FavoriteProvider favoriteProvider);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "emailAddress", source = "email.value")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
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
    target
        .getUserServices()
        .removeIf(
            existing ->
                serviceProvider.getUserServices().stream()
                    .noneMatch(
                        service ->
                            service
                                .getServiceTypeId()
                                .value()
                                .equals(existing.getId().getServiceTypeId())));
    serviceProvider
        .getUserServices()
        .forEach(
            userService ->
                target.getUserServices().stream()
                    .filter(
                        existing ->
                            existing
                                .getId()
                                .getServiceTypeId()
                                .equals(userService.getServiceTypeId().value()))
                    .findFirst()
                    .ifPresentOrElse(
                        existing -> updateUserServiceJpa(existing, userService),
                        () -> addUserService(target, userService)));
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "yearOfExperience", source = "yearOfExperience.value")
  @Mapping(target = "userDocument", source = "userDocument.value")
  void updateUserServiceJpa(@MappingTarget UserServiceJpa target, UserService source);

  default void fromPortfolioItemDomain(
      ServiceProvider serviceProvider, @MappingTarget ServiceProviderJpa target) {
    target
        .getPortfolioItems()
        .removeIf(
            jpa ->
                serviceProvider.getPortfolioItems().stream()
                    .noneMatch(item -> item.getId().value().equals(jpa.getId())));
    serviceProvider
        .getPortfolioItems()
        .forEach(
            item ->
                target.getPortfolioItems().stream()
                    .filter(jpa -> jpa.getId().equals(item.getId().value()))
                    .findFirst()
                    .ifPresentOrElse(
                        jpa -> updatePortfolioItemJpa(jpa, item),
                        () -> addPortfolioItemJpa(target, item)));
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "title", source = "title.value")
  @Mapping(target = "description", source = "description.value")
  @Mapping(target = "mediaId", source = "mediaId.value")
  void updatePortfolioItemJpa(@MappingTarget PortfolioItemJpa target, PortfolioItem source);

  private void addUserService(ServiceProviderJpa target, UserService userService) {
    UserServiceJpa userServiceJpa = toUserServiceJpa(userService);
    userServiceJpa.setServiceProvider(target);
    target.getUserServices().add(userServiceJpa);
  }

  default void addPortfolioItemJpa(
      ServiceProviderJpa serviceProviderJpa, PortfolioItem portfolioItem) {

    PortfolioItemJpa portfolioJpa = toPortfolioItemJpa(portfolioItem);

    portfolioJpa.setServiceProvider(serviceProviderJpa);

    serviceProviderJpa.getPortfolioItems().add(portfolioJpa);
  }

  default void mapPortfolioItemsForInsert(
      ServiceProvider serviceProvider, @MappingTarget ServiceProviderJpa target) {
    serviceProvider.getPortfolioItems().forEach(item -> addPortfolioItemJpa(target, item));
  }

  default ServiceProvider toServiceProviderDomain(ServiceProviderJpa serviceProviderJpa) {
    return ServiceProvider.reconstitute(
        new ServiceProviderId(serviceProviderJpa.getId()),
        new UserId(serviceProviderJpa.getUserId()),
        new ServiceProviderState(
            new ProviderProfile(
                new ProviderContact(
                    new ProviderLocation(
                        new UserCityId(serviceProviderJpa.getCity()),
                        new UserDistrictId(serviceProviderJpa.getDistrict()),
                        serviceProviderJpa.getQuarter() != null
                            ? new UserQuarterId(serviceProviderJpa.getQuarter())
                            : null),
                    new PhoneNumber(
                        serviceProviderJpa.getPhoneNumber().getCountryCode(),
                        serviceProviderJpa.getPhoneNumber().getNumber())),
                serviceProviderJpa.getAbout() != null
                    ? new AboutProvider(serviceProviderJpa.getAbout())
                    : null),
            new ProviderReview(
                ServiceProviderStatus.valueOf(serviceProviderJpa.getStatus()),
                serviceProviderJpa.getApprovedBy() != null
                    ? new UserId(serviceProviderJpa.getApprovedBy())
                    : null,
                serviceProviderJpa.getRejectedBy() != null
                    ? new UserId(serviceProviderJpa.getRejectedBy())
                    : null,
                serviceProviderJpa.getRejectionReason() != null
                    ? toRejectionReason(serviceProviderJpa.getRejectionReason())
                    : null),
            new ProviderAudit(
                new CreatedAt(serviceProviderJpa.getCreatedAt()),
                serviceProviderJpa.getUpdatedAt() != null
                    ? new CreatedAt(serviceProviderJpa.getUpdatedAt())
                    : null),
            toIdentityVerificationDomain(serviceProviderJpa.getIdentityVerification()),
            new ProviderImages(
                serviceProviderJpa.getProfileImageId() != null
                    ? ProfileImageMediaId.from(serviceProviderJpa.getProfileImageId())
                    : null,
                serviceProviderJpa.getPendingProfileImageId() != null
                    ? ProfileImageMediaId.from(serviceProviderJpa.getPendingProfileImageId())
                    : null,
                serviceProviderJpa.getProfileImageReviewStatus() != null
                    ? ProfileImageReviewStatus.valueOf(
                        serviceProviderJpa.getProfileImageReviewStatus())
                    : ProfileImageReviewStatus.NONE,
                serviceProviderJpa.getProfileImageRejectionReason() != null
                    ? RejectionReason.valueOf(serviceProviderJpa.getProfileImageRejectionReason())
                    : null),
            new ServiceCollections(
                toUserServiceDomain(serviceProviderJpa.getUserServices()),
                toPortfolioItemDomain(serviceProviderJpa.getPortfolioItems()))));
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

  default List<PortfolioItem> toPortfolioItemDomain(List<PortfolioItemJpa> portfolioItems) {
    return portfolioItems.stream()
        .map(
            item ->
                PortfolioItem.reconstitute(
                    new PortfolioItemId(item.getId()),
                    new PortfolioItemTitle(item.getTitle()),
                    new PortfolioItemDescription(item.getDescription()),
                    new PortfolioItemMediaId(item.getMediaId()),
                    new CreatedAt(item.getCreatedAt())))
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
  @Mapping(target = "rejectionReason", source = "identityVerification.rejectionReason")
  @Mapping(target = "about", source = "about.value")
  @Mapping(target = "profileImageId", source = "profileImageId.value")
  @Mapping(target = "pendingProfileImageId", source = "pendingProfileImageId.value")
  @Mapping(target = "profileImageReviewStatus", source = "profileImageReviewStatus")
  @Mapping(target = "profileImageRejectionReason", source = "profileImageRejectionReason")
  @Mapping(target = "identityVerification", source = "identityVerification")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  void toServiceProviderJpa(
      @MappingTarget ServiceProviderJpa serviceProviderJpa, ServiceProvider serviceProvider);

  default void fromServiceProvider(
      @MappingTarget ServiceProviderJpa serviceProviderJpa, ServiceProvider serviceProvider) {
    toServiceProviderJpa(serviceProviderJpa, serviceProvider);
    if (serviceProviderJpa.getIdentityVerification() != null) {
      serviceProviderJpa.getIdentityVerification().setServiceProvider(serviceProviderJpa);
    }
    fromUserServicesDomain(serviceProvider, serviceProviderJpa);
    fromPortfolioItemDomain(serviceProvider, serviceProviderJpa);
  }

  default IdentityVerification toIdentityVerificationDomain(IdentityVerificationJpa jpa) {
    if (jpa == null) {
      return IdentityVerification.reconstitute(
          IdentityVerificationId.generate(),
          new IdentityVerificationDocuments(null, null),
          IdentityVerificationReview.pending());
    }
    return IdentityVerification.reconstitute(
        IdentityVerificationId.from(jpa.getId()),
        new IdentityVerificationDocuments(
            jpa.getCniRectoId() != null ? CniRectoMediaId.from(jpa.getCniRectoId()) : null,
            jpa.getCniVersoId() != null ? CniVersoMediaId.from(jpa.getCniVersoId()) : null),
        new IdentityVerificationReview(
            IdentityVerificationStatus.valueOf(jpa.getStatus()),
            jpa.getRejectionReason() != null ? toRejectionReason(jpa.getRejectionReason()) : null,
            jpa.getVerifiedAt() != null ? VerifiedAt.from(jpa.getVerifiedAt()) : null,
            jpa.getVerifiedBy() != null ? UserId.from(jpa.getVerifiedBy()) : null));
  }

  default RejectionReason toRejectionReason(String value) {
    try {
      return RejectionReason.valueOf(value);
    } catch (IllegalArgumentException _) {
      return null;
    }
  }

  default PortfolioView toPortfolioView(PortfolioItemJpa item) {
    return new PortfolioView(
        item.getId(),
        item.getTitle(),
        item.getDescription(),
        item.getMediaId(),
        item.getCreatedAt());
  }

  default ServiceProviderView2 toServiceProviderView(
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

    List<PortfolioView> portfolios =
        serviceProviderJpa.getPortfolioItems().stream().map(this::toPortfolioView).toList();

    return new ServiceProviderView2(
        serviceProviderJpa.getId(),
        serviceProviderJpa.getUserId(),
        new ServiceProviderViews.UserView(
            userJpa.getId(),
            userJpa.getFirstname(),
            userJpa.getLastname(),
            userJpa.getEmailAddress(),
            userJpa.getCreatedAt()),
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
        serviceProviderJpa.getProfileImageId(),
        serviceProviderJpa.getPendingProfileImageId(),
        serviceProviderJpa.getProfileImageReviewStatus() != null
            ? ProfileImageReviewStatus.valueOf(serviceProviderJpa.getProfileImageReviewStatus())
            : ProfileImageReviewStatus.NONE,
        serviceProviderJpa.getIdentityVerification() != null
                && serviceProviderJpa.getIdentityVerification().getStatus() != null
            ? IdentityVerificationStatus.valueOf(
                serviceProviderJpa.getIdentityVerification().getStatus())
            : IdentityVerificationStatus.valueOf(serviceProviderJpa.getStatus()),
        serviceProviderJpa.getCreatedAt(),
        serviceProviderJpa.getUpdatedAt(),
        services,
        portfolios,
        ServiceProviderStatus.APPROVED.name().equals(serviceProviderJpa.getStatus())
                || serviceProviderJpa.getIdentityVerification() == null
            ? null
            : toIdentityVerificationView(serviceProviderJpa.getIdentityVerification()));
  }

  default ServiceTypeView toServiceTypeView(ServiceTypeJpa serviceTypeJpa) {
    return new ServiceTypeView(
        serviceTypeJpa.getId(),
        serviceTypeJpa.getName(),
        serviceTypeJpa.getCategory(),
        serviceTypeJpa.isActive());
  }

  default List<UserServiceView> toUserServiceViews(
      List<UserServiceJpa> userServices, List<ServiceTypeJpa> serviceTypes) {
    Map<UUID, ServiceTypeJpa> serviceTypesById =
        serviceTypes.stream().collect(Collectors.toMap(ServiceTypeJpa::getId, Function.identity()));
    return userServices.stream()
        .map(
            userService ->
                new UserServiceView(
                    toServiceTypeView(serviceTypesById.get(userService.getId().getServiceTypeId())),
                    userService.getYearOfExperience(),
                    userService.getUserDocument(),
                    userService.getCreatedAt()))
        .toList();
  }

  default ServiceProviderView2 toServiceProviderView2(
      ServiceProviderJpa serviceProviderJpa,
      UserJpa userJpa,
      List<UserServiceView> services,
      List<PortfolioItemJpa> portfolioItems) {
    List<PortfolioView> portfolios = portfolioItems.stream().map(this::toPortfolioView).toList();

    return new ServiceProviderView2(
        serviceProviderJpa.getId(),
        serviceProviderJpa.getUserId(),
        new ServiceProviderViews.UserView(
            userJpa.getId(),
            userJpa.getFirstname(),
            userJpa.getLastname(),
            userJpa.getEmailAddress(),
            userJpa.getCreatedAt()),
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
        serviceProviderJpa.getProfileImageId(),
        serviceProviderJpa.getPendingProfileImageId(),
        serviceProviderJpa.getProfileImageReviewStatus() != null
            ? ProfileImageReviewStatus.valueOf(serviceProviderJpa.getProfileImageReviewStatus())
            : ProfileImageReviewStatus.NONE,
        serviceProviderJpa.getIdentityVerification() != null
                && serviceProviderJpa.getIdentityVerification().getStatus() != null
            ? IdentityVerificationStatus.valueOf(
                serviceProviderJpa.getIdentityVerification().getStatus())
            : IdentityVerificationStatus.valueOf(serviceProviderJpa.getStatus()),
        serviceProviderJpa.getCreatedAt(),
        serviceProviderJpa.getUpdatedAt(),
        services,
        portfolios,
        ServiceProviderStatus.APPROVED.name().equals(serviceProviderJpa.getStatus())
                || serviceProviderJpa.getIdentityVerification() == null
            ? null
            : toIdentityVerificationView(serviceProviderJpa.getIdentityVerification()));
  }

  default ServiceProviderVerificationView toServiceProviderVerificationView(
      ServiceProviderJpa serviceProviderJpa,
      UserJpa userJpa,
      List<UserServiceView> services,
      List<PortfolioItemJpa> portfolioItems) {
    return new ServiceProviderVerificationView(
        serviceProviderJpa.getId(),
        serviceProviderJpa.getUserId(),
        new ServiceProviderViews.UserView(
            userJpa.getId(),
            userJpa.getFirstname(),
            userJpa.getLastname(),
            userJpa.getEmailAddress(),
            userJpa.getCreatedAt()),
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
        serviceProviderJpa.getProfileImageId(),
        serviceProviderJpa.getPendingProfileImageId(),
        serviceProviderJpa.getProfileImageReviewStatus() != null
            ? ProfileImageReviewStatus.valueOf(serviceProviderJpa.getProfileImageReviewStatus())
            : ProfileImageReviewStatus.NONE,
        serviceProviderJpa.getCreatedAt(),
        serviceProviderJpa.getUpdatedAt(),
        services,
        portfolioItems.stream().map(this::toPortfolioView).toList(),
        ServiceProviderStatus.APPROVED.name().equals(serviceProviderJpa.getStatus())
            ? null
            : toIdentityVerificationView(serviceProviderJpa.getIdentityVerification()));
  }

  default ServiceProviderViews.ServiceProviderView3 toServiceProviderView3(
      ServiceProviderJpa serviceProviderJpa, UserJpa userJpa, List<UserServiceView> services) {
    return new ServiceProviderViews.ServiceProviderView3(
        serviceProviderJpa.getId(),
        serviceProviderJpa.getUserId(),
        new ServiceProviderViews.UserView(
            userJpa.getId(),
            userJpa.getFirstname(),
            userJpa.getLastname(),
            userJpa.getEmailAddress(),
            userJpa.getCreatedAt()),
        PhoneNumber.from(
            serviceProviderJpa.getPhoneNumber().getCountryCode(),
            serviceProviderJpa.getPhoneNumber().getNumber()),
        serviceProviderJpa.getCity(),
        serviceProviderJpa.getDistrict(),
        serviceProviderJpa.getQuarter(),
        serviceProviderJpa.getAbout(),
        ServiceProviderStatus.valueOf(serviceProviderJpa.getStatus()),
        Objects.requireNonNull(serviceProviderJpa.getProfileImageId()),
        Objects.requireNonNull(serviceProviderJpa.getPendingProfileImageId()),
        serviceProviderJpa.getProfileImageReviewStatus() != null
            ? ProfileImageReviewStatus.valueOf(serviceProviderJpa.getProfileImageReviewStatus())
            : ProfileImageReviewStatus.NONE,
        serviceProviderJpa.getProfileImageRejectionReason() != null
            ? RejectionReason.valueOf(serviceProviderJpa.getProfileImageRejectionReason())
            : null,
        serviceProviderJpa.getUpdatedAt(),
        toIdentityVerificationView(serviceProviderJpa.getIdentityVerification()),
        serviceProviderJpa.getCreatedAt(),
        serviceProviderJpa.getUpdatedAt(),
        services);
  }

  default IdentityVerificationView toIdentityVerificationView(IdentityVerificationJpa jpa) {
    return new IdentityVerificationView(
        Objects.requireNonNull(jpa.getId()),
        jpa.getCniRectoId(),
        jpa.getCniVersoId(),
        IdentityVerificationStatus.valueOf(jpa.getStatus()),
        jpa.getRejectionReason() != null ? toRejectionReason(jpa.getRejectionReason()) : null,
        jpa.getVerifiedAt(),
        jpa.getVerifiedBy());
  }

  default ServiceProviderIdentityView toServiceProviderIdentityView(
      ServiceProviderJpa serviceProviderJpa) {
    return new ServiceProviderIdentityView(
        serviceProviderJpa.getId(),
        serviceProviderJpa.getProfileImageId(),
        this.toIdentityVerificationView(serviceProviderJpa.getIdentityVerification()));
  }

  default ServiceProviderViews.ServiceProviderView1 toServiceProviderView1(
      ServiceProviderJpa serviceProviderJpa, UserJpa userJpa) {
    return new ServiceProviderViews.ServiceProviderView1(
        serviceProviderJpa.getId(),
        serviceProviderJpa.getUserId(),
        new ServiceProviderViews.UserView(
            userJpa.getId(),
            userJpa.getFirstname(),
            userJpa.getLastname(),
            userJpa.getEmailAddress(),
            userJpa.getCreatedAt()),
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
        serviceProviderJpa.getProfileImageId(),
        serviceProviderJpa.getCreatedAt(),
        serviceProviderJpa.getUpdatedAt());
  }
}
