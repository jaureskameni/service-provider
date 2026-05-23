package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
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
    jpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));

    // When
    ServiceProvider domain = objectUnderTest.toServiceProviderDomain(jpa);

    // Then
    assertThat(domain).isNotNull();
    assertThat(domain.getLocation())
        .satisfies(
            location -> {
              assertThat(location.cityId().value()).isEqualTo(cityId);
              assertThat(location.districtId().value()).isEqualTo(districtId);
              assertThat(location.quarterId().value()).isEqualTo(quarterId);
            });
  }

  @Test
  void toServiceTypeView1_shouldMapCorrectly() {
    // Given
    ServiceTypeJpa jpa = new ServiceTypeJpa();
    jpa.setId(UUID.randomUUID());
    jpa.setName("Plumber");
    jpa.setCategory("MAINTENANCE");
    jpa.setActive(true);

    // When
    var view = objectUnderTest.toServiceTypeView1(jpa);

    // Then
    assertThat(view).isNotNull();
    assertThat(view.getId()).isEqualTo(jpa.getId());
    assertThat(view.getName()).isEqualTo(jpa.getName());
    assertThat(view.getCategory()).isEqualTo(jpa.getCategory());
    assertThat(view.getIsActive()).isTrue();
  }
}
