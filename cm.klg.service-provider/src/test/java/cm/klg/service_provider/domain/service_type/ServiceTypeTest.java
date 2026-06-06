package cm.klg.service_provider.domain.service_type;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ServiceTypeTest {

  @Test
  void shouldCreateServiceTypeUsingOf() {
    // Given
    ServiceTypeName name = ServiceTypeName.from("Electricien");
    ServiceCategory category = ServiceCategory.BATIMENT_MAINTENANCE;

    // When
    ServiceType serviceType = ServiceType.of(name, category, true);

    // Then
    assertThat(serviceType.getServiceTypeId()).isNotNull();
    assertThat(serviceType.getServiceTypeName()).isEqualTo(name);
    assertThat(serviceType.getServiceCategory()).isEqualTo(category);
    assertThat(serviceType.isActive()).isTrue();
    assertThat(serviceType.getCreatedAt()).isNotNull();
    assertThat(serviceType.getUpdated()).isNull();
  }

  @Test
  void shouldReconstituteServiceType() {
    // Given
    ServiceTypeId id = ServiceTypeId.generate();
    ServiceTypeName name = ServiceTypeName.from("Electricien");
    ServiceCategory category = ServiceCategory.BATIMENT_MAINTENANCE;
    CreatedAt createdAt = CreatedAt.from(LocalDateTime.now().minusDays(1));
    CreatedAt updatedAt = CreatedAt.from(LocalDateTime.now());

    // When
    ServiceType serviceType = new ServiceType(id, name, category, true, createdAt, updatedAt);

    // Then
    assertThat(serviceType.getServiceTypeId()).isEqualTo(id);
    assertThat(serviceType.getServiceTypeName()).isEqualTo(name);
    assertThat(serviceType.getServiceCategory()).isEqualTo(category);
    assertThat(serviceType.isActive()).isTrue();
    assertThat(serviceType.getCreatedAt()).isEqualTo(createdAt);
    assertThat(serviceType.getUpdated()).isEqualTo(updatedAt);
  }
}
