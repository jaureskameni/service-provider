package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView1;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderAudit;
import cm.klg.service_provider.domain.service_provider.ProviderContact;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ProviderReview;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.UserService;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.mapstruct.AfterMapping;
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
  @Mapping(target = "phoneNumber", source = "phoneNumber")
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
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  ServiceProviderJpa toServiceProviderJpa(ServiceProvider serviceProvider);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id.serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "id.serviceTypeId", source = "serviceTypeId.value")
  @Mapping(target = "userDocument", source = "userDocument.value")
  @Mapping(target = "yearOfExperience", source = "yearOfExperience.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  UserServiceJpa toUserServiceJpa(UserService userService);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "emailAddress", source = "email.value")
  @Mapping(target = "phoneNumber", source = "phoneNumber")
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
        new UserId(userJpa.getId()), userProfile, CreatedAt.from(userJpa.getCreatedAt()));
  }

  @AfterMapping
  default void mapUserServicesJpa(
      ServiceProvider serviceProvider, @MappingTarget ServiceProviderJpa target) {
    target.setUserServices(
        serviceProvider.getUserServices().stream().map(this::toUserServiceJpa).toList());
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
                    : null),
            new ProviderAudit(
                new CreatedAt(serviceProviderJpa.getCreatedAt()),
                serviceProviderJpa.getUpdatedAt() != null
                    ? new CreatedAt(serviceProviderJpa.getUpdatedAt())
                    : null),
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
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  void toServiceProviderJpa(
      @MappingTarget ServiceProviderJpa serviceProviderJpa, ServiceProvider serviceProvider);

  default ServiceProviderView1 toServiceProviderView1(ServiceProviderJpa serviceProviderJpa) {
    return new ServiceProviderView1() {
      @Override
      public UUID getId() {
        return serviceProviderJpa.getId();
      }

      @Override
      public UUID getUserId() {
        return serviceProviderJpa.getUserId();
      }

      @Override
      public UUID getCityId() {
        return serviceProviderJpa.getCity();
      }

      @Override
      public UUID getDistrictId() {
        return serviceProviderJpa.getDistrict();
      }

      @Override
      public UUID getQuarterId() {
        return serviceProviderJpa.getQuarter();
      }

      @Override
      @Nullable
      public UUID getApproveBy() {
        return serviceProviderJpa.getApprovedBy();
      }

      @Override
      @Nullable
      public UUID getRejectBy() {
        return serviceProviderJpa.getRejectedBy();
      }

      @Override
      public PhoneNumber getPhoneNumber() {
        return PhoneNumber.from(
            serviceProviderJpa.getPhoneNumber().getCountryCode(),
            serviceProviderJpa.getPhoneNumber().getNumber());
      }

      @Override
      public String getStatus() {
        return serviceProviderJpa.getStatus();
      }

      @Override
      public LocalDateTime getCreatedAt() {
        return serviceProviderJpa.getCreatedAt();
      }

      @Override
      public LocalDateTime getUpdatedAt() {
        return serviceProviderJpa.getUpdatedAt();
      }

      @Override
      public List<UserServiceView> getUserService() {
        return serviceProviderJpa.getUserServices().stream()
            .map(JpaMapper.this::toUserServiceView)
            .toList();
      }
    };
  }

  default UserServiceView toUserServiceView(UserServiceJpa userServiceJpa) {
    return new UserServiceView() {
      @Override
      public UUID getServiceProviderId() {
        return userServiceJpa.getId().getServiceProviderId();
      }

      @Override
      public UUID getServiceTypeId() {
        return userServiceJpa.getId().getServiceTypeId();
      }

      @Override
      public int getYearOfExperience() {
        return userServiceJpa.getYearOfExperience();
      }

      @Override
      public UUID getUserDocument() {
        return userServiceJpa.getUserDocument();
      }

      @Override
      public LocalDateTime getCreatedAt() {
        return userServiceJpa.getCreatedAt();
      }
    };
  }

  default ServiceTypeView1 toServiceTypeView1(ServiceTypeJpa serviceTypeJpa) {
    return new ServiceTypeView1() {
      @Override
      public java.util.UUID getId() {
        return serviceTypeJpa.getId();
      }

      @Override
      public String getName() {
        return serviceTypeJpa.getName();
      }

      @Override
      public String getCategory() {
        return serviceTypeJpa.getCategory();
      }

      @Override
      public boolean getIsActive() {
        return serviceTypeJpa.isActive();
      }
    };
  }
}
