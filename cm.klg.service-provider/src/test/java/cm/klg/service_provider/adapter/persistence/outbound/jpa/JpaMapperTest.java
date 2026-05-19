package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderAudit;
import cm.klg.service_provider.domain.service_provider.ProviderContact;
import cm.klg.service_provider.domain.service_provider.ProviderReview;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserService;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpaMapperTest {

  private final JpaMapper objectUnderTest = new JpaMapperImpl();

  @Test
  void toUserJpa_shouldMapAllFieldsCorrectly() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    Firstname firstname = Firstname.from("John");
    Lastname lastname = Lastname.from("Doe");
    EmailAddress email = EmailAddress.from("john.doe@example.com");
    PhoneNumber phoneNumber = new PhoneNumber("+1", "1234567890");
    var createdAt = CreatedAt.from(LocalDateTime.now());
    PhoneNumberJpa phoneNumberJpa =
        new PhoneNumberJpa(phoneNumber.countryCode(), phoneNumber.number());

    UserProfile userProfile = new UserProfile(firstname, lastname, email, phoneNumber);
    User user = User.reconstitute(userId, userProfile, createdAt);

    UserJpa expectedUserJpa = new UserJpa();
    expectedUserJpa.setId(userId.value());
    expectedUserJpa.setFirstname(firstname.value());
    expectedUserJpa.setLastname(lastname.value());
    expectedUserJpa.setEmailAddress(email.value());
    expectedUserJpa.setPhoneNumber(phoneNumberJpa);
    expectedUserJpa.setCreatedAt(createdAt.value());

    // When
    UserJpa userJpa = objectUnderTest.toUserJpa(user);

    // Then
    assertThat(userJpa).usingRecursiveComparison().isEqualTo(expectedUserJpa);
  }

  @Test
  void toServiceProviderJpa_shouldMapAllFieldsCorrectly() {
    // Given
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    UserId userId = new UserId(UUID.randomUUID());
    UserCityId cityId = new UserCityId(UUID.randomUUID());
    UserDistrictId districtId = new UserDistrictId(UUID.randomUUID());
    PhoneNumber phoneNumber = new PhoneNumber("+237", "678901234");
    ServiceProviderStatus status = ServiceProviderStatus.PENDING;
    UserId approvedBy = new UserId(UUID.randomUUID());
    UserId rejectedBy = new UserId(UUID.randomUUID());
    var createdAt = CreatedAt.from(LocalDateTime.now());
    var updatedAt = CreatedAt.from(LocalDateTime.now());
    PhoneNumberJpa phoneNumberJpa =
        new PhoneNumberJpa(phoneNumber.countryCode(), phoneNumber.number());

    ServiceTypeId serviceTypeId = new ServiceTypeId(UUID.randomUUID());
    YearOfExperience yearOfExperience = new YearOfExperience(5);
    UserDocument document = new UserDocument(UUID.randomUUID());
    UserService userService =
        new UserService(serviceProviderId, serviceTypeId, yearOfExperience, document, createdAt);

    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            serviceProviderId,
            userId,
            new ProviderContact(cityId, districtId, phoneNumber),
            new ProviderReview(status, approvedBy, rejectedBy),
            new ProviderAudit(createdAt, updatedAt),
            List.of(userService));

    UserServiceJpaId userServiceJpaId = new UserServiceJpaId();
    userServiceJpaId.setServiceProviderId(serviceProviderId.value());
    userServiceJpaId.setServiceTypeId(serviceTypeId.value());

    UserServiceJpa expectedUserServiceJpa = new UserServiceJpa();
    expectedUserServiceJpa.setId(userServiceJpaId);
    expectedUserServiceJpa.setYearOfExperience(yearOfExperience.value());
    expectedUserServiceJpa.setUserDocument(document.value());
    expectedUserServiceJpa.setCreatedAt(createdAt.value());

    ServiceProviderJpa expectedServiceProviderJpa = new ServiceProviderJpa();
    expectedServiceProviderJpa.setId(serviceProviderId.value());
    expectedServiceProviderJpa.setUserId(userId.value());
    expectedServiceProviderJpa.setCity(cityId.value());
    expectedServiceProviderJpa.setDistrict(districtId.value());
    expectedServiceProviderJpa.setPhoneNumber(phoneNumberJpa);
    expectedServiceProviderJpa.setStatus(status.name());
    expectedServiceProviderJpa.setApprovedBy(approvedBy.value());
    expectedServiceProviderJpa.setRejectedBy(rejectedBy.value());
    expectedServiceProviderJpa.setCreatedAt(createdAt.value());
    expectedServiceProviderJpa.setUpdatedAt(updatedAt.value());
    expectedServiceProviderJpa.setUserServices(List.of(expectedUserServiceJpa));

    // When
    ServiceProviderJpa serviceProviderJpa = objectUnderTest.toServiceProviderJpa(serviceProvider);

    // Then
    assertThat(serviceProviderJpa).usingRecursiveComparison().isEqualTo(expectedServiceProviderJpa);
  }

  @Test
  void toUserServiceJpa_shouldMapAllFieldsCorrectly() {
    // Given
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    ServiceTypeId serviceTypeId = new ServiceTypeId(UUID.randomUUID());
    YearOfExperience yearOfExperience = new YearOfExperience(10);
    UserDocument document = new UserDocument(UUID.randomUUID());
    CreatedAt createdAt = CreatedAt.from(LocalDateTime.now());

    UserService userService =
        new UserService(serviceProviderId, serviceTypeId, yearOfExperience, document, createdAt);

    UserServiceJpaId expectedUserServiceJpaId = new UserServiceJpaId();
    expectedUserServiceJpaId.setServiceProviderId(serviceProviderId.value());
    expectedUserServiceJpaId.setServiceTypeId(serviceTypeId.value());

    UserServiceJpa expectedUserServiceJpa = new UserServiceJpa();
    expectedUserServiceJpa.setId(expectedUserServiceJpaId);
    expectedUserServiceJpa.setYearOfExperience(yearOfExperience.value());
    expectedUserServiceJpa.setUserDocument(document.value());
    expectedUserServiceJpa.setCreatedAt(createdAt.value());

    // When
    UserServiceJpa userServiceJpa = objectUnderTest.toUserServiceJpa(userService);

    // Then
    assertThat(userServiceJpa).usingRecursiveComparison().isEqualTo(expectedUserServiceJpa);
  }

  @Test
  void toUserDomain_shouldMapAllFieldsCorrectly() {
    // Given
    UUID userId = UUID.randomUUID();
    String firstname = "Jane";
    String lastname = "Doe";
    String emailAddress = "jane.doe@example.com";
    String countryCode = "+44";
    String number = "9876543210";
    LocalDateTime createdAt = LocalDateTime.now();

    UserJpa userJpa = new UserJpa();
    userJpa.setId(userId);
    userJpa.setFirstname(firstname);
    userJpa.setLastname(lastname);
    userJpa.setEmailAddress(emailAddress);
    userJpa.setPhoneNumber(new PhoneNumberJpa(countryCode, number));
    userJpa.setCreatedAt(createdAt);

    UserProfile expectedUserProfile =
        new UserProfile(
            Firstname.from(firstname),
            Lastname.from(lastname),
            EmailAddress.from(emailAddress),
            PhoneNumber.from(countryCode, number));
    User expectedUser =
        User.reconstitute(new UserId(userId), expectedUserProfile, new CreatedAt(createdAt));

    // When
    User user = objectUnderTest.toUserDomain(userJpa);

    // Then
    assertThat(user).usingRecursiveComparison().isEqualTo(expectedUser);
  }

  @Test
  void toServiceProviderView1_shouldMapAllFieldsCorrectly() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID approvedBy = UUID.randomUUID();
    UUID serviceTypeId = UUID.randomUUID();
    UUID documentId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = createdAt.plusDays(1);

    UserServiceJpaId userServiceJpaId = new UserServiceJpaId();
    userServiceJpaId.setServiceProviderId(serviceProviderId);
    userServiceJpaId.setServiceTypeId(serviceTypeId);

    UserServiceJpa userServiceJpa = new UserServiceJpa();
    userServiceJpa.setId(userServiceJpaId);
    userServiceJpa.setUserDocument(documentId);
    userServiceJpa.setYearOfExperience(7);
    userServiceJpa.setCreatedAt(createdAt);

    ServiceProviderJpa serviceProviderJpa = new ServiceProviderJpa();
    serviceProviderJpa.setId(serviceProviderId);
    serviceProviderJpa.setUserId(userId);
    serviceProviderJpa.setCity(cityId);
    serviceProviderJpa.setDistrict(districtId);
    serviceProviderJpa.setApprovedBy(approvedBy);
    serviceProviderJpa.setRejectedBy(null);
    serviceProviderJpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
    serviceProviderJpa.setStatus("APPROVED");
    serviceProviderJpa.setCreatedAt(createdAt);
    serviceProviderJpa.setUpdatedAt(updatedAt);
    serviceProviderJpa.setUserServices(List.of(userServiceJpa));

    // When
    var result = objectUnderTest.toServiceProviderView1(serviceProviderJpa);

    // Then
    assertThat(result.getId()).isEqualTo(serviceProviderId);
    assertThat(result.getUserId()).isEqualTo(userId);
    assertThat(result.getCityId()).isEqualTo(cityId);
    assertThat(result.getDistrictId()).isEqualTo(districtId);
    assertThat(result.getApproveBy()).isEqualTo(approvedBy);
    assertThat(result.getRejectBy()).isNull();
    assertThat(result.getPhoneNumber()).isEqualTo(PhoneNumber.from("+237", "678901234"));
    assertThat(result.getStatus()).isEqualTo("APPROVED");
    assertThat(result.getCreatedAt()).isEqualTo(createdAt);
    assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    assertThat(result.getUserService()).hasSize(1);
    assertThat(result.getUserService().getFirst().getServiceProviderId())
        .isEqualTo(serviceProviderId);
    assertThat(result.getUserService().getFirst().getServiceTypeId()).isEqualTo(serviceTypeId);
    assertThat(result.getUserService().getFirst().getYearOfExperience()).isEqualTo(7);
    assertThat(result.getUserService().getFirst().getUserDocument()).isEqualTo(documentId);
    assertThat(result.getUserService().getFirst().getCreatedAt()).isEqualTo(createdAt);
  }
}
