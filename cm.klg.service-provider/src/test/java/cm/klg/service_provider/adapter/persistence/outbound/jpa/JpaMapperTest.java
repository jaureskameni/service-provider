package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.favorite.FavoriteProvider;
import cm.klg.service_provider.domain.favorite.FavoriteProviderId;
import cm.klg.service_provider.domain.provider_client.ProviderClient;
import cm.klg.service_provider.domain.provider_client.ProviderClientId;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.PortfolioItem;
import cm.klg.service_provider.domain.service_provider.PortfolioItemDescription;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemMediaId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemTitle;
import cm.klg.service_provider.domain.service_provider.ProviderAudit;
import cm.klg.service_provider.domain.service_provider.ProviderContact;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ProviderReview;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceCollections;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.UserService;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceCategory;
import cm.klg.service_provider.domain.service_type.ServiceType;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.service_type.ServiceTypeName;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpaMapperTest {

  private final JpaMapper objectUnderTest = new JpaMapperImpl();

  @Nested
  class UserMapping {

    @Test
    void toUserJpa_shouldMapAllFields() {
      var userId = UUID.randomUUID();
      var now = LocalDateTime.now();
      var user =
          User.reconstitute(
              new UserId(userId),
              new UserProfile(
                  Firstname.from("John"),
                  Lastname.from("Doe"),
                  EmailAddress.from("john@example.com"),
                  PhoneNumber.from("+237", "678901234")),
              true,
              CreatedAt.from(now));

      UserJpa result = objectUnderTest.toUserJpa(user);

      assertThat(result.getId()).isEqualTo(userId);
      assertThat(result.getFirstname()).isEqualTo("John");
      assertThat(result.getLastname()).isEqualTo("Doe");
      assertThat(result.getEmailAddress()).isEqualTo("john@example.com");
      assertThat(result.getPhoneNumber().getCountryCode()).isEqualTo("+237");
      assertThat(result.getPhoneNumber().getNumber()).isEqualTo("678901234");
      assertThat(result.isServiceProvider()).isTrue();
      assertThat(result.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void toUserJpa_shouldMergeIntoExistingTarget() {
      var userId = UUID.randomUUID();
      var user =
          User.reconstitute(
              new UserId(userId),
              new UserProfile(
                  Firstname.from("Jane"),
                  Lastname.from("Smith"),
                  EmailAddress.from("jane@example.com"),
                  PhoneNumber.from("+237", "123456789")),
              false,
              CreatedAt.from(LocalDateTime.now()));

      UserJpa target = new UserJpa();
      target.setId(userId);
      objectUnderTest.toUserJpa(user, target);

      assertThat(target.getFirstname()).isEqualTo("Jane");
      assertThat(target.getLastname()).isEqualTo("Smith");
    }

    @Test
    void toUserDomain_shouldMapAllFields() {
      var userId = UUID.randomUUID();
      var now = LocalDateTime.now();
      UserJpa jpa = new UserJpa();
      jpa.setId(userId);
      jpa.setFirstname("John");
      jpa.setLastname("Doe");
      jpa.setEmailAddress("john@example.com");
      jpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
      jpa.setServiceProvider(true);
      jpa.setCreatedAt(now);

      User result = objectUnderTest.toUserDomain(jpa);

      assertThat(result.getId().value()).isEqualTo(userId);
      assertThat(result.getFirstname().value()).isEqualTo("John");
      assertThat(result.getLastname().value()).isEqualTo("Doe");
      assertThat(result.getEmail().value()).isEqualTo("john@example.com");
      assertThat(result.getPhoneNumber().countryCode()).isEqualTo("+237");
      assertThat(result.getPhoneNumber().number()).isEqualTo("678901234");
      assertThat(result.isServiceProvider()).isTrue();
    }
  }

  @Nested
  class ServiceProviderJpaMapping {

    @Test
    void toServiceProviderJpa_shouldMapHierarchicalLocationCorrectly() {
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

      ServiceProviderJpa jpa = objectUnderTest.toServiceProviderJpa(serviceProvider);

      assertThat(jpa).isNotNull();
      assertThat(jpa.getCity()).isEqualTo(cityId);
      assertThat(jpa.getDistrict()).isEqualTo(districtId);
      assertThat(jpa.getQuarter()).isEqualTo(quarterId);
    }

    @Test
    void toServiceProviderJpa_shouldMapAllScalarFields() {
      UUID userId = UUID.randomUUID();
      UUID approvedBy = UUID.randomUUID();
      ServiceProvider serviceProvider =
          ServiceProvider.reconstitute(
              ServiceProviderId.generate(),
              new UserId(userId),
              new ProviderContact(
                  new ProviderLocation(
                      new UserCityId(UUID.randomUUID()),
                      new UserDistrictId(UUID.randomUUID()),
                      new UserQuarterId(UUID.randomUUID())),
                  new PhoneNumber("+237", "698765432")),
              new ProviderReview(
                  ServiceProviderStatus.APPROVED, new UserId(approvedBy), null, null),
              new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
              new AboutProvider("I am a plumber"),
              new ServiceCollections(List.of(), List.of()));

      ServiceProviderJpa jpa = objectUnderTest.toServiceProviderJpa(serviceProvider);

      assertThat(jpa.getId()).isEqualTo(serviceProvider.getId().value());
      assertThat(jpa.getUserId()).isEqualTo(userId);
      assertThat(jpa.getStatus()).isEqualTo("APPROVED");
      assertThat(jpa.getApprovedBy()).isEqualTo(approvedBy);
      assertThat(jpa.getPhoneNumber().getNumber()).isEqualTo("698765432");
      assertThat(jpa.getAbout()).isEqualTo("I am a plumber");
    }

    @Test
    void toServiceProviderJpa_shouldMapRejectionReasonAndRejectedBy() {
      UUID rejectedBy = UUID.randomUUID();
      ServiceProvider serviceProvider =
          ServiceProvider.reconstitute(
              ServiceProviderId.generate(),
              new UserId(UUID.randomUUID()),
              new ProviderContact(
                  new ProviderLocation(
                      new UserCityId(UUID.randomUUID()),
                      new UserDistrictId(UUID.randomUUID()),
                      new UserQuarterId(UUID.randomUUID())),
                  new PhoneNumber("+237", "698765432")),
              new ProviderReview(
                  ServiceProviderStatus.REJECTED,
                  null,
                  new UserId(rejectedBy),
                  new RejectionReason("Invalid documents")),
              new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
              null,
              new ServiceCollections(List.of(), List.of()));

      ServiceProviderJpa jpa = objectUnderTest.toServiceProviderJpa(serviceProvider);

      assertThat(jpa.getStatus()).isEqualTo("REJECTED");
      assertThat(jpa.getRejectedBy()).isEqualTo(rejectedBy);
      assertThat(jpa.getRejectionReason()).isEqualTo("Invalid documents");
    }

    @Test
    void toServiceProviderJpa_shouldMapUserServices() {
      var spId = ServiceProviderId.generate();
      var serviceTypeId1 = new ServiceTypeId(UUID.randomUUID());
      var serviceTypeId2 = new ServiceTypeId(UUID.randomUUID());
      ServiceProvider serviceProvider =
          ServiceProvider.reconstitute(
              spId,
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
              new ServiceCollections(
                  List.of(
                      UserService.of(
                          spId,
                          serviceTypeId1,
                          new YearOfExperience(3),
                          new UserDocument(UUID.randomUUID())),
                      UserService.of(
                          spId,
                          serviceTypeId2,
                          new YearOfExperience(5),
                          new UserDocument(UUID.randomUUID()))),
                  List.of()));

      ServiceProviderJpa jpa = objectUnderTest.toServiceProviderJpa(serviceProvider);

      assertThat(jpa.getUserServices()).hasSize(2);
      assertThat(jpa.getUserServices())
          .extracting(us -> us.getId().getServiceTypeId())
          .containsExactlyInAnyOrder(serviceTypeId1.value(), serviceTypeId2.value());
      jpa.getUserServices().forEach(us -> assertThat(us.getServiceProvider()).isSameAs(jpa));
    }

    @Test
    void toServiceProviderJpa_shouldMapPortfolioItems() {
      var spId = ServiceProviderId.generate();
      var title1 = PortfolioItemTitle.from("Project A");
      var title2 = PortfolioItemTitle.from("Project B");
      PortfolioItem item1 =
          PortfolioItem.of(
              title1,
              PortfolioItemDescription.from("Description A"),
              new PortfolioItemMediaId(UUID.randomUUID()));
      PortfolioItem item2 =
          PortfolioItem.of(
              title2,
              PortfolioItemDescription.from("Description B"),
              new PortfolioItemMediaId(UUID.randomUUID()));
      ServiceProvider serviceProvider =
          ServiceProvider.reconstitute(
              spId,
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
              new ServiceCollections(List.of(), List.of(item1, item2)));

      ServiceProviderJpa jpa = objectUnderTest.toServiceProviderJpa(serviceProvider);

      assertThat(jpa.getPortfolioItems()).hasSize(2);
      assertThat(jpa.getPortfolioItems())
          .extracting(PortfolioItemJpa::getTitle)
          .containsExactlyInAnyOrder("Project A", "Project B");
      jpa.getPortfolioItems().forEach(pi -> assertThat(pi.getServiceProvider()).isSameAs(jpa));
    }

    @Test
    void toServiceProviderJpa_shouldHandleEmptyUserServicesAndPortfolioItems() {
      ServiceProvider serviceProvider =
          ServiceProvider.of(
              new UserId(UUID.randomUUID()),
              new ProviderLocation(
                  new UserCityId(UUID.randomUUID()),
                  new UserDistrictId(UUID.randomUUID()),
                  new UserQuarterId(UUID.randomUUID())),
              new PhoneNumber("+237", "678901234"),
              null,
              List.of());

      ServiceProviderJpa jpa = objectUnderTest.toServiceProviderJpa(serviceProvider);

      assertThat(jpa.getUserServices()).isEmpty();
      assertThat(jpa.getPortfolioItems()).isEmpty();
    }
  }

  @Nested
  class ServiceProviderDomainMapping {

    @Test
    void toServiceProviderDomain_shouldMapHierarchicalLocationCorrectly() {
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

      ServiceProvider domain = objectUnderTest.toServiceProviderDomain(jpa);

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
    void toServiceProviderDomain_shouldMapAllScalarFields() {
      var spId = UUID.randomUUID();
      var userId = UUID.randomUUID();
      var approvedBy = UUID.randomUUID();
      var now = LocalDateTime.now();
      ServiceProviderJpa jpa = new ServiceProviderJpa();
      jpa.setId(spId);
      jpa.setUserId(userId);
      jpa.setCity(UUID.randomUUID());
      jpa.setDistrict(UUID.randomUUID());
      jpa.setQuarter(UUID.randomUUID());
      jpa.setStatus("APPROVED");
      jpa.setApprovedBy(approvedBy);
      jpa.setAbout("Professional plumber");
      jpa.setPhoneNumber(new PhoneNumberJpa("+237", "698765432"));
      jpa.setCreatedAt(now);

      ServiceProvider domain = objectUnderTest.toServiceProviderDomain(jpa);

      assertThat(domain.getId().value()).isEqualTo(spId);
      assertThat(domain.getUserId().value()).isEqualTo(userId);
      assertThat(domain.getStatus()).isEqualTo(ServiceProviderStatus.APPROVED);
      assertThat(domain.getApprovedBy().value()).isEqualTo(approvedBy);
      assertThat(domain.getAbout().value()).isEqualTo("Professional plumber");
      assertThat(domain.getPhoneNumber().number()).isEqualTo("698765432");
    }

    @Test
    void toServiceProviderDomain_shouldMapRejectionFields() {
      var rejectedBy = UUID.randomUUID();
      ServiceProviderJpa jpa = new ServiceProviderJpa();
      jpa.setId(UUID.randomUUID());
      jpa.setUserId(UUID.randomUUID());
      jpa.setCity(UUID.randomUUID());
      jpa.setDistrict(UUID.randomUUID());
      jpa.setQuarter(UUID.randomUUID());
      jpa.setStatus("REJECTED");
      jpa.setRejectedBy(rejectedBy);
      jpa.setRejectionReason("Invalid documents");
      jpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
      jpa.setCreatedAt(LocalDateTime.now());
      jpa.setUpdatedAt(LocalDateTime.now());

      ServiceProvider domain = objectUnderTest.toServiceProviderDomain(jpa);

      assertThat(domain.getStatus()).isEqualTo(ServiceProviderStatus.REJECTED);
      assertThat(domain.getRejectedBy().value()).isEqualTo(rejectedBy);
      assertThat(domain.getRejectionReason().value()).isEqualTo("Invalid documents");
      assertThat(domain.getUpdatedAt()).isNotNull();
    }

    @Test
    void toServiceProviderDomain_shouldMapUserServices() {
      var spId = UUID.randomUUID();
      var stId1 = UUID.randomUUID();
      var stId2 = UUID.randomUUID();
      var now = LocalDateTime.now();
      ServiceProviderJpa jpa = new ServiceProviderJpa();
      jpa.setId(spId);
      jpa.setUserId(UUID.randomUUID());
      jpa.setCity(UUID.randomUUID());
      jpa.setDistrict(UUID.randomUUID());
      jpa.setQuarter(UUID.randomUUID());
      jpa.setStatus("PENDING");
      jpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
      jpa.setCreatedAt(now);

      UserServiceJpa us1 = new UserServiceJpa();
      UserServiceJpaId id1 = new UserServiceJpaId();
      id1.setServiceProviderId(spId);
      id1.setServiceTypeId(stId1);
      us1.setId(id1);
      us1.setYearOfExperience(3);
      us1.setUserDocument(UUID.randomUUID());
      us1.setCreatedAt(now);

      UserServiceJpa us2 = new UserServiceJpa();
      UserServiceJpaId id2 = new UserServiceJpaId();
      id2.setServiceProviderId(spId);
      id2.setServiceTypeId(stId2);
      us2.setId(id2);
      us2.setYearOfExperience(5);
      us2.setUserDocument(UUID.randomUUID());
      us2.setCreatedAt(now);

      jpa.setUserServices(List.of(us1, us2));

      ServiceProvider domain = objectUnderTest.toServiceProviderDomain(jpa);

      assertThat(domain.getUserServices()).hasSize(2);
      assertThat(domain.getUserServices())
          .extracting(us -> us.getServiceTypeId().value())
          .containsExactlyInAnyOrder(stId1, stId2);
    }

    @Test
    void toServiceProviderDomain_shouldMapPortfolioItems() {
      var spId = UUID.randomUUID();
      var mediaId1 = UUID.randomUUID();
      var mediaId2 = UUID.randomUUID();
      var now = LocalDateTime.now();
      ServiceProviderJpa jpa = new ServiceProviderJpa();
      jpa.setId(spId);
      jpa.setUserId(UUID.randomUUID());
      jpa.setCity(UUID.randomUUID());
      jpa.setDistrict(UUID.randomUUID());
      jpa.setQuarter(UUID.randomUUID());
      jpa.setStatus("PENDING");
      jpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
      jpa.setCreatedAt(now);

      PortfolioItemJpa pi1 = new PortfolioItemJpa();
      pi1.setId(UUID.randomUUID());
      pi1.setTitle("Project A");
      pi1.setDescription("Description A");
      pi1.setMediaId(mediaId1);
      pi1.setCreatedAt(now);

      PortfolioItemJpa pi2 = new PortfolioItemJpa();
      pi2.setId(UUID.randomUUID());
      pi2.setTitle("Project B");
      pi2.setDescription("Description B");
      pi2.setMediaId(mediaId2);
      pi2.setCreatedAt(now);

      jpa.setPortfolioItems(List.of(pi1, pi2));

      ServiceProvider domain = objectUnderTest.toServiceProviderDomain(jpa);

      assertThat(domain.getPortfolioItems()).hasSize(2);
      assertThat(domain.getPortfolioItems())
          .extracting(item -> item.getTitle().value())
          .containsExactlyInAnyOrder("Project A", "Project B");
      assertThat(domain.getPortfolioItems())
          .extracting(item -> item.getMediaId().value())
          .containsExactlyInAnyOrder(mediaId1, mediaId2);
    }

    @Test
    void toServiceProviderDomain_shouldHandleNullOptionalFields() {
      var now = LocalDateTime.now();
      ServiceProviderJpa jpa = new ServiceProviderJpa();
      jpa.setId(UUID.randomUUID());
      jpa.setUserId(UUID.randomUUID());
      jpa.setCity(UUID.randomUUID());
      jpa.setDistrict(UUID.randomUUID());
      jpa.setQuarter(UUID.randomUUID());
      jpa.setStatus("PENDING");
      jpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
      jpa.setCreatedAt(now);

      ServiceProvider domain = objectUnderTest.toServiceProviderDomain(jpa);

      assertThat(domain.getAbout()).isNull();
      assertThat(domain.getApprovedBy()).isNull();
      assertThat(domain.getRejectedBy()).isNull();
      assertThat(domain.getRejectionReason()).isNull();
      assertThat(domain.getUpdatedAt()).isNull();
      assertThat(domain.getUserServices()).isEmpty();
      assertThat(domain.getPortfolioItems()).isEmpty();
    }
  }

  @Nested
  class PortfolioItemMapping {

    @Test
    void toPortfolioItemJpa_shouldMapAllFields() {
      var itemId = PortfolioItemId.generate();
      var mediaId = new PortfolioItemMediaId(UUID.randomUUID());
      var now = CreatedAt.from(LocalDateTime.now());
      var item =
          PortfolioItem.reconstitute(
              itemId,
              PortfolioItemTitle.from("My project"),
              PortfolioItemDescription.from("A great project"),
              mediaId,
              now);

      PortfolioItemJpa jpa = objectUnderTest.toPortfolioItemJpa(item);

      assertThat(jpa.getId()).isEqualTo(itemId.value());
      assertThat(jpa.getTitle()).isEqualTo("My project");
      assertThat(jpa.getDescription()).isEqualTo("A great project");
      assertThat(jpa.getMediaId()).isEqualTo(mediaId.value());
      assertThat(jpa.getCreatedAt()).isEqualTo(now.value());
    }

    @Test
    void toPortfolioItemDomain_shouldMapAllFields() {
      var itemId = UUID.randomUUID();
      var mediaId = UUID.randomUUID();
      var now = LocalDateTime.now();
      PortfolioItemJpa jpa = new PortfolioItemJpa();
      jpa.setId(itemId);
      jpa.setTitle("My project");
      jpa.setDescription("A great project");
      jpa.setMediaId(mediaId);
      jpa.setCreatedAt(now);

      List<PortfolioItem> items = objectUnderTest.toPortfolioItemDomain(List.of(jpa));

      assertThat(items).hasSize(1);
      PortfolioItem item = items.get(0);
      assertThat(item.getId().value()).isEqualTo(itemId);
      assertThat(item.getTitle().value()).isEqualTo("My project");
      assertThat(item.getDescription().value()).isEqualTo("A great project");
      assertThat(item.getMediaId().value()).isEqualTo(mediaId);
      assertThat(item.getCreatedAt().value()).isEqualTo(now);
    }

    @Test
    void toPortfolioItemDomain_shouldHandleEmptyList() {
      List<PortfolioItem> items = objectUnderTest.toPortfolioItemDomain(List.of());
      assertThat(items).isEmpty();
    }

    @Test
    void toPortfolioView_shouldMapAllFields() {
      var itemId = UUID.randomUUID();
      var mediaId = UUID.randomUUID();
      var now = LocalDateTime.now();
      var item = new PortfolioItemJpa();
      item.setId(itemId);
      item.setTitle("My project");
      item.setDescription("A great project");
      item.setMediaId(mediaId);
      item.setCreatedAt(now);

      PortfolioView result = objectUnderTest.toPortfolioView(item);

      assertThat(result.id()).isEqualTo(itemId);
      assertThat(result.title()).isEqualTo("My project");
      assertThat(result.description()).isEqualTo("A great project");
      assertThat(result.mediaId()).isEqualTo(mediaId);
      assertThat(result.createdAt()).isEqualTo(now);
    }
  }

  @Nested
  class UserServiceMapping {

    @Test
    void toUserServiceJpa_shouldMapAllFields() {
      var spId = ServiceProviderId.generate();
      var stId = new ServiceTypeId(UUID.randomUUID());
      var docId = new UserDocument(UUID.randomUUID());
      var now = CreatedAt.from(LocalDateTime.now());
      var userService = UserService.reconstitute(spId, stId, new YearOfExperience(5), docId, now);

      UserServiceJpa jpa = objectUnderTest.toUserServiceJpa(userService);

      assertThat(jpa.getId().getServiceProviderId()).isEqualTo(spId.value());
      assertThat(jpa.getId().getServiceTypeId()).isEqualTo(stId.value());
      assertThat(jpa.getYearOfExperience()).isEqualTo(5);
      assertThat(jpa.getUserDocument()).isEqualTo(docId.value());
      assertThat(jpa.getCreatedAt()).isEqualTo(now.value());
    }

    @Test
    void toUserServiceDomain_shouldMapAllFields() {
      var spId = UUID.randomUUID();
      var stId = UUID.randomUUID();
      var docId = UUID.randomUUID();
      var now = LocalDateTime.now();
      UserServiceJpa jpa = new UserServiceJpa();
      UserServiceJpaId id = new UserServiceJpaId();
      id.setServiceProviderId(spId);
      id.setServiceTypeId(stId);
      jpa.setId(id);
      jpa.setYearOfExperience(5);
      jpa.setUserDocument(docId);
      jpa.setCreatedAt(now);

      List<UserService> services = objectUnderTest.toUserServiceDomain(List.of(jpa));

      assertThat(services).hasSize(1);
      UserService service = services.get(0);
      assertThat(service.getServiceProviderId().value()).isEqualTo(spId);
      assertThat(service.getServiceTypeId().value()).isEqualTo(stId);
      assertThat(service.getYearOfExperience().value()).isEqualTo(5);
      assertThat(service.getUserDocument().value()).isEqualTo(docId);
      assertThat(service.getCreatedAt().value()).isEqualTo(now);
    }
  }

  @Nested
  class ServiceTypeMapping {

    @Test
    void toServiceTypeJpa_shouldMapAllFields() {
      var stId = ServiceTypeId.generate();
      var now = CreatedAt.from(LocalDateTime.now());
      var serviceType =
          new ServiceType(
              stId,
              ServiceTypeName.from("Plumber"),
              ServiceCategory.BATIMENT_MAINTENANCE,
              true,
              now,
              null);

      ServiceTypeJpa jpa = objectUnderTest.toServiceTypeJpa(serviceType);

      assertThat(jpa.getId()).isEqualTo(stId.value());
      assertThat(jpa.getName()).isEqualTo("Plumber");
      assertThat(jpa.getCategory()).isEqualTo("BATIMENT_MAINTENANCE");
      assertThat(jpa.isActive()).isTrue();
      assertThat(jpa.getCreatedAt()).isEqualTo(now.value());
    }

    @Test
    void toServiceTypeJpa_shouldMapUpdatedAt() {
      var now = CreatedAt.from(LocalDateTime.now());
      var serviceType =
          new ServiceType(
              ServiceTypeId.generate(),
              ServiceTypeName.from("Plumber"),
              ServiceCategory.BATIMENT_MAINTENANCE,
              true,
              now,
              now);

      ServiceTypeJpa jpa = objectUnderTest.toServiceTypeJpa(serviceType);

      assertThat(jpa.getUpdatedAt()).isEqualTo(now.value());
    }

    @Test
    void toServiceTypeView_shouldMapCorrectly() {
      ServiceTypeJpa jpa = new ServiceTypeJpa();
      jpa.setId(UUID.randomUUID());
      jpa.setName("Plumber");
      jpa.setCategory("MAINTENANCE");
      jpa.setActive(true);

      var view = objectUnderTest.toServiceTypeView(jpa);

      assertThat(view).isNotNull();
      assertThat(view.id()).isEqualTo(jpa.getId());
      assertThat(view.name()).isEqualTo(jpa.getName());
      assertThat(view.category()).isEqualTo(jpa.getCategory());
      assertThat(view.isActive()).isTrue();
    }
  }

  @Nested
  class ProviderClientMapping {

    @Test
    void toJpa_shouldMapProviderClientToJpa() {
      var clientId = ProviderClientId.generate();
      var userId = new UserId(UUID.randomUUID());
      var providerId = ServiceProviderId.generate();
      var now = CreatedAt.from(LocalDateTime.now());
      var client = new ProviderClient(clientId, userId, providerId, now);

      ProviderClientJpa jpa = objectUnderTest.toJpa(client);

      assertThat(jpa.getId()).isEqualTo(clientId.value());
      assertThat(jpa.getUserId()).isEqualTo(userId.value());
      assertThat(jpa.getProviderId()).isEqualTo(providerId.value());
      assertThat(jpa.getCreatedAt()).isEqualTo(now.value());
    }
  }

  @Nested
  class FavoriteProviderMapping {

    @Test
    void toJpa_shouldMapFavoriteProviderToJpa() {
      var favoriteId = FavoriteProviderId.generate();
      var userId = new UserId(UUID.randomUUID());
      var providerId = ServiceProviderId.generate();
      var now = CreatedAt.from(LocalDateTime.now());
      var favorite = new FavoriteProvider(favoriteId, userId, providerId, now);

      FavoriteProviderJpa jpa = objectUnderTest.toJpa(favorite);

      assertThat(jpa.getId()).isEqualTo(favoriteId.value());
      assertThat(jpa.getUserId()).isEqualTo(userId.value());
      assertThat(jpa.getProviderId()).isEqualTo(providerId.value());
      assertThat(jpa.getCreatedAt()).isEqualTo(now.value());
    }
  }

  @Nested
  class ServiceProviderUpdate {

    @Test
    void updateServiceProviderJpa_shouldKeepManagedCollectionAndAddOnlyMissingChildren() {
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

      objectUnderTest.fromServiceProvider(target, source);

      assertThat(target.getUserServices()).isSameAs(managedCollection);
      assertThat(target.getUserServices()).contains(existingChild);
      assertThat(existingChild.getYearOfExperience()).isEqualTo(1);
      assertThat(target.getUserServices())
          .extracting(userService -> userService.getId().getServiceTypeId())
          .containsExactlyInAnyOrder(existingServiceTypeId, newServiceTypeId);
      assertThat(
              target.getUserServices().stream()
                  .filter(
                      userService ->
                          newServiceTypeId.equals(userService.getId().getServiceTypeId()))
                  .findFirst()
                  .orElseThrow()
                  .getServiceProvider())
          .isSameAs(target);
    }

    @Test
    void fromServiceProvider_shouldAddOnlyNewPortfolioItems() {
      var spId = UUID.randomUUID();
      var existingItemId = UUID.randomUUID();
      var newItemId = UUID.randomUUID();
      var now = LocalDateTime.now();

      PortfolioItem existingItem =
          PortfolioItem.reconstitute(
              PortfolioItemId.from(existingItemId),
              PortfolioItemTitle.from("Existing"),
              PortfolioItemDescription.from("Existing description"),
              new PortfolioItemMediaId(UUID.randomUUID()),
              CreatedAt.from(now));
      PortfolioItem newItem =
          PortfolioItem.reconstitute(
              PortfolioItemId.from(newItemId),
              PortfolioItemTitle.from("New"),
              PortfolioItemDescription.from("New description"),
              new PortfolioItemMediaId(UUID.randomUUID()),
              CreatedAt.from(now));

      ServiceProvider source =
          ServiceProvider.reconstitute(
              new ServiceProviderId(spId),
              new UserId(UUID.randomUUID()),
              new ProviderContact(
                  new ProviderLocation(
                      new UserCityId(UUID.randomUUID()),
                      new UserDistrictId(UUID.randomUUID()),
                      new UserQuarterId(UUID.randomUUID())),
                  new PhoneNumber("+237", "678901234")),
              new ProviderReview(ServiceProviderStatus.PENDING, null, null, null),
              new ProviderAudit(CreatedAt.from(now), null),
              null,
              new ServiceCollections(List.of(), List.of(existingItem, newItem)));

      ServiceProviderJpa target = new ServiceProviderJpa();
      target.setId(spId);
      PortfolioItemJpa existingJpa = new PortfolioItemJpa();
      existingJpa.setId(existingItemId);
      existingJpa.setServiceProvider(target);
      target.getPortfolioItems().add(existingJpa);
      List<PortfolioItemJpa> managedPortfolioItems = target.getPortfolioItems();

      objectUnderTest.fromServiceProvider(target, source);

      assertThat(target.getPortfolioItems()).isSameAs(managedPortfolioItems);
      assertThat(target.getPortfolioItems()).contains(existingJpa);
      assertThat(target.getPortfolioItems()).hasSize(2);
      assertThat(target.getPortfolioItems())
          .extracting(PortfolioItemJpa::getId)
          .containsExactlyInAnyOrder(existingItemId, newItemId);
    }

    @Test
    void fromServiceProvider_shouldUpdateScalarFields() {
      var spId = UUID.randomUUID();
      var now = CreatedAt.from(LocalDateTime.now());
      ServiceProvider source =
          ServiceProvider.reconstitute(
              new ServiceProviderId(spId),
              new UserId(UUID.randomUUID()),
              new ProviderContact(
                  new ProviderLocation(
                      new UserCityId(UUID.randomUUID()),
                      new UserDistrictId(UUID.randomUUID()),
                      new UserQuarterId(UUID.randomUUID())),
                  new PhoneNumber("+237", "123456789")),
              new ProviderReview(
                  ServiceProviderStatus.APPROVED, new UserId(UUID.randomUUID()), null, null),
              new ProviderAudit(now, now),
              new AboutProvider("Updated about"),
              new ServiceCollections(List.of(), List.of()));

      ServiceProviderJpa target = new ServiceProviderJpa();
      target.setId(spId);
      target.setCity(UUID.randomUUID());
      target.setPhoneNumber(new PhoneNumberJpa("+237", "987654321"));

      objectUnderTest.fromServiceProvider(target, source);

      assertThat(target.getPhoneNumber().getNumber()).isEqualTo("123456789");
      assertThat(target.getStatus()).isEqualTo("APPROVED");
      assertThat(target.getAbout()).isEqualTo("Updated about");
    }

    @Test
    void fromServiceProvider_shouldHandleAlreadyUpToDateCollections() {
      var spId = UUID.randomUUID();
      var stId = UUID.randomUUID();
      ServiceProvider source =
          ServiceProvider.reconstitute(
              new ServiceProviderId(spId),
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
              new ServiceCollections(
                  List.of(
                      UserService.reconstitute(
                          new ServiceProviderId(spId),
                          new ServiceTypeId(stId),
                          new YearOfExperience(5),
                          new UserDocument(UUID.randomUUID()),
                          CreatedAt.from(LocalDateTime.now()))),
                  List.of()));

      ServiceProviderJpa target = new ServiceProviderJpa();
      target.setId(spId);
      UserServiceJpa existingJpa = new UserServiceJpa();
      UserServiceJpaId existingJpaId = new UserServiceJpaId();
      existingJpaId.setServiceProviderId(spId);
      existingJpaId.setServiceTypeId(stId);
      existingJpa.setId(existingJpaId);
      existingJpa.setServiceProvider(target);
      target.getUserServices().add(existingJpa);

      objectUnderTest.fromServiceProvider(target, source);

      assertThat(target.getUserServices()).hasSize(1);
    }
  }

  @Nested
  class ServiceProviderViewMapping {

    @Test
    void toServiceProviderView_shouldMapAllFieldsCorrectly() {
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

      var view = objectUnderTest.toServiceProviderView(spJpa, userJpa, List.of(stJpa));

      assertThat(view.id()).isEqualTo(spId);
      assertThat(view.userId()).isEqualTo(userId);
      assertThat(view.firstname()).isEqualTo("John");
      assertThat(view.lastname()).isEqualTo("Doe");
      assertThat(view.phoneNumber().number()).isEqualTo("678901234");
      assertThat(view.status()).isEqualTo("APPROVED");
      assertThat(view.createdAt()).isEqualTo(now);
      assertThat(view.services()).hasSize(1);
      var serviceView = view.services().get(0);
      assertThat(serviceView.serviceType().name()).isEqualTo("Plumber");
      assertThat(serviceView.yearOfExperience()).isEqualTo(5);
      assertThat(serviceView.document()).isEqualTo(docId);
    }

    @Test
    void toServiceProviderView_shouldMapOptionalFields() {
      var approvedBy = UUID.randomUUID();
      var rejectedBy = UUID.randomUUID();
      var districtId = UUID.randomUUID();
      var quarterId = UUID.randomUUID();
      var now = LocalDateTime.now();
      ServiceProviderJpa spJpa = new ServiceProviderJpa();
      spJpa.setId(UUID.randomUUID());
      spJpa.setUserId(UUID.randomUUID());
      spJpa.setCity(UUID.randomUUID());
      spJpa.setDistrict(districtId);
      spJpa.setQuarter(quarterId);
      spJpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
      spJpa.setStatus("REJECTED");
      spJpa.setApprovedBy(approvedBy);
      spJpa.setRejectedBy(rejectedBy);
      spJpa.setRejectionReason("Invalid");
      spJpa.setCreatedAt(now);
      spJpa.setUpdatedAt(now);
      spJpa.setAbout("About");

      UserJpa userJpa = new UserJpa();
      userJpa.setId(UUID.randomUUID());
      userJpa.setFirstname("John");
      userJpa.setLastname("Doe");

      var view = objectUnderTest.toServiceProviderView(spJpa, userJpa, List.of());

      assertThat(view.approvedBy()).isEqualTo(approvedBy);
      assertThat(view.rejectedBy()).isEqualTo(rejectedBy);
      assertThat(view.rejectionReason()).isEqualTo("Invalid");
      assertThat(view.about()).isEqualTo("About");
      assertThat(view.updatedAt()).isEqualTo(now);
      assertThat(view.districtId()).isEqualTo(districtId);
      assertThat(view.quarterId()).isEqualTo(quarterId);
    }
  }

  @Nested
  class HelperMethods {

    @Test
    void addPortfolioItemJpa_shouldAddItemToCollection() {
      var portfolioItem =
          PortfolioItem.of(
              PortfolioItemTitle.from("Project"),
              PortfolioItemDescription.from("Description"),
              new PortfolioItemMediaId(UUID.randomUUID()));
      ServiceProviderJpa target = new ServiceProviderJpa();
      target.setId(UUID.randomUUID());

      objectUnderTest.addPortfolioItemJpa(target, portfolioItem);

      assertThat(target.getPortfolioItems()).hasSize(1);
      PortfolioItemJpa added = target.getPortfolioItems().get(0);
      assertThat(added.getId()).isEqualTo(portfolioItem.getId().value());
      assertThat(added.getServiceProvider()).isSameAs(target);
    }

    @Test
    void mapUserServicesForInsert_shouldAddAllUserServices() {
      var spId = ServiceProviderId.generate();
      ServiceProvider serviceProvider =
          ServiceProvider.reconstitute(
              spId,
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
              new ServiceCollections(
                  List.of(
                      UserService.of(
                          spId,
                          new ServiceTypeId(UUID.randomUUID()),
                          new YearOfExperience(3),
                          new UserDocument(UUID.randomUUID())),
                      UserService.of(
                          spId,
                          new ServiceTypeId(UUID.randomUUID()),
                          new YearOfExperience(5),
                          new UserDocument(UUID.randomUUID()))),
                  List.of()));
      ServiceProviderJpa target = new ServiceProviderJpa();
      target.setId(spId.value());

      objectUnderTest.mapUserServicesForInsert(serviceProvider, target);

      assertThat(target.getUserServices()).hasSize(2);
      target.getUserServices().forEach(us -> assertThat(us.getServiceProvider()).isSameAs(target));
    }
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
        new ServiceCollections(List.of(), List.of()));
  }
}
