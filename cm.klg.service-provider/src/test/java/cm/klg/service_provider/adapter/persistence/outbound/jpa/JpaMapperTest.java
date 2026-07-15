package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.entity.PhoneNumberJpa;
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
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
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
  void toServiceProviderJpa_shouldMapHierarchicalLocationCorrectly() {
    // Given
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID quarterId = UUID.randomUUID();

    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new ProviderLocation(
                new UserCityId(cityId),
                new UserDistrictId(districtId),
                new UserQuarterId(quarterId)),
            new PhoneNumber("+237", "678901234"),
            null,
            List.of());

    // When
    ServiceProviderJpa jpa = objectUnderTest.toServiceProviderJpa(serviceProvider);

    // Then
    assertThat(jpa).isNotNull();
    assertThat(jpa.getCity()).isEqualTo(cityId);
    assertThat(jpa.getDistrict()).isEqualTo(districtId);
    assertThat(jpa.getQuarter()).isEqualTo(quarterId);
  }

  @Test
  void toServiceProviderDomain_shouldMapHierarchicalLocationCorrectly() {
    // Given
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID quarterId = UUID.randomUUID();

    ServiceProviderJpa jpa = new ServiceProviderJpa();
    jpa.setId(UUID.randomUUID());
    jpa.setUserId(UUID.randomUUID());
    jpa.setCity(cityId);
    jpa.setDistrict(districtId);
    jpa.setQuarter(quarterId);
    jpa.setStatus("PENDING");
    jpa.setCreatedAt(LocalDateTime.now());
    jpa.setAbout("About me");
    jpa.setRejectionReason("Reason");
    jpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));

    // When
    ServiceProvider domain = objectUnderTest.toServiceProviderDomain(jpa);

    // Then
    assertThat(domain).isNotNull();
    assertThat(domain.getAbout().value()).isEqualTo("About me");
    assertThat(domain.getRejectionReason().value()).isEqualTo("Reason");
    assertThat(domain.getLocation())
        .satisfies(
            location -> {
              assertThat(location.cityId().value()).isEqualTo(cityId);
              assertThat(location.districtId().value()).isEqualTo(districtId);
              assertThat(location.quarterId().value()).isEqualTo(quarterId);
            });
  }

  @Test
  void updateServiceProviderJpa_shouldKeepManagedCollectionAndAddOnlyMissingChildren() {
    // Given
    UUID serviceProviderId = UUID.randomUUID();
    UUID existingServiceTypeId = UUID.randomUUID();
    UUID newServiceTypeId = UUID.randomUUID();
    ServiceProvider source = reconstitutedServiceProvider(serviceProviderId);
    source.addUserService(
        new ServiceTypeId(existingServiceTypeId),
        new YearOfExperience(8),
        new UserDocument(UUID.randomUUID()));
    source.addUserService(
        new ServiceTypeId(newServiceTypeId),
        new YearOfExperience(2),
        new UserDocument(UUID.randomUUID()));

    ServiceProviderJpa target = new ServiceProviderJpa();
    target.setId(serviceProviderId);
    UserServiceJpa existingChild = new UserServiceJpa();
    UserServiceJpaId existingChildId = new UserServiceJpaId();
    existingChildId.setServiceProviderId(serviceProviderId);
    existingChildId.setServiceTypeId(existingServiceTypeId);
    existingChild.setId(existingChildId);
    existingChild.setYearOfExperience(1);
    existingChild.setServiceProvider(target);
    target.getUserServices().add(existingChild);
    List<UserServiceJpa> managedCollection = target.getUserServices();

    // When
    objectUnderTest.fromServiceProvider(target, source);

    // Then
    assertThat(target.getUserServices()).isSameAs(managedCollection);
    assertThat(target.getUserServices()).contains(existingChild);
    assertThat(existingChild.getYearOfExperience()).isEqualTo(1);
    assertThat(target.getUserServices())
        .extracting(userService -> userService.getId().getServiceTypeId())
        .containsExactlyInAnyOrder(existingServiceTypeId, newServiceTypeId);
    assertThat(
            target.getUserServices().stream()
                .filter(
                    userService -> newServiceTypeId.equals(userService.getId().getServiceTypeId()))
                .findFirst()
                .orElseThrow()
                .getServiceProvider())
        .isSameAs(target);
  }

  @Test
  void toServiceTypeView_shouldMapCorrectly() {
    // Given
    ServiceTypeJpa jpa = new ServiceTypeJpa();
    jpa.setId(UUID.randomUUID());
    jpa.setName("Plumber");
    jpa.setCategory("MAINTENANCE");
    jpa.setActive(true);

    // When
    var view = objectUnderTest.toServiceTypeView(jpa);

    // Then
    assertThat(view).isNotNull();
    assertThat(view.id()).isEqualTo(jpa.getId());
    assertThat(view.name()).isEqualTo(jpa.getName());
    assertThat(view.category()).isEqualTo(jpa.getCategory());
    assertThat(view.isActive()).isTrue();
  }

  @Test
  void toServiceProviderView_shouldMapAllFieldsCorrectly() {
    // Given
    UUID spId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID cityId = UUID.randomUUID();
    UUID stId = UUID.randomUUID();
    UUID docId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    ServiceProviderJpa spJpa = new ServiceProviderJpa();
    spJpa.setId(spId);
    spJpa.setUserId(userId);
    spJpa.setCity(cityId);
    spJpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
    spJpa.setStatus("APPROVED");
    spJpa.setCreatedAt(now);

    UserServiceJpa usJpa = new UserServiceJpa();
    UserServiceJpaId usId = new UserServiceJpaId();
    usId.setServiceProviderId(spId);
    usId.setServiceTypeId(stId);
    usJpa.setId(usId);
    usJpa.setYearOfExperience(5);
    usJpa.setUserDocument(docId);
    usJpa.setCreatedAt(now);
    spJpa.setUserServices(List.of(usJpa));

    UserJpa userJpa = new UserJpa();
    userJpa.setId(userId);
    userJpa.setFirstname("John");
    userJpa.setLastname("Doe");

    ServiceTypeJpa stJpa = new ServiceTypeJpa();
    stJpa.setId(stId);
    stJpa.setName("Plumber");
    stJpa.setCategory("MAINTENANCE");

    // When
    var view = objectUnderTest.toServiceProviderView(spJpa, userJpa, List.of(stJpa));

    // Then
    assertThat(view.id()).isEqualTo(spId);
    assertThat(view.userId()).isEqualTo(userId);
    assertThat(view.firstname()).isEqualTo("John");
    assertThat(view.lastname()).isEqualTo("Doe");
    assertThat(view.phoneNumber().number()).isEqualTo("678901234");
    assertThat(view.services()).hasSize(1);
    var serviceView = view.services().get(0);
    assertThat(serviceView.serviceType().name()).isEqualTo("Plumber");
    assertThat(serviceView.yearOfExperience()).isEqualTo(5);
    assertThat(serviceView.document()).isEqualTo(docId);
  }

  private ServiceProvider reconstitutedServiceProvider(UUID serviceProviderId) {
    return ServiceProvider.reconstitute(
        new ServiceProviderId(serviceProviderId),
        new UserId(UUID.randomUUID()),
        new ProviderContact(
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234")),
        new ProviderReview(ServiceProviderStatus.PENDING, null, null, null),
        new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
        null,
        List.of());
  }
}
