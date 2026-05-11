package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProvider;
import cm.klg.service_provider.domain.ServiceProvider.UserService;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
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
  @Mapping(target = "city", source = "city.value")
  @Mapping(target = "district", source = "district.value")
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
}
