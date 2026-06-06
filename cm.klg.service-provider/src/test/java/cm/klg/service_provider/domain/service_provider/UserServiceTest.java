package cm.klg.service_provider.domain.service_provider;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  @Test
  void shouldCreateUserServiceUsingOf() {
    // Given
    ServiceProviderId serviceProviderId = ServiceProviderId.generate();
    ServiceTypeId serviceTypeId = ServiceTypeId.generate();
    YearOfExperience yearOfExperience = new YearOfExperience(5);
    UserDocument userDocument = new UserDocument(java.util.UUID.randomUUID());

    // When
    UserService userService =
        UserService.of(serviceProviderId, serviceTypeId, yearOfExperience, userDocument);

    // Then
    assertThat(userService.getServiceProviderId()).isEqualTo(serviceProviderId);
    assertThat(userService.getServiceTypeId()).isEqualTo(serviceTypeId);
    assertThat(userService.getYearOfExperience()).isEqualTo(yearOfExperience);
    assertThat(userService.getUserDocument()).isEqualTo(userDocument);
    assertThat(userService.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldReconstituteUserService() {
    // Given
    ServiceProviderId serviceProviderId = ServiceProviderId.generate();
    ServiceTypeId serviceTypeId = ServiceTypeId.generate();
    YearOfExperience yearOfExperience = new YearOfExperience(5);
    UserDocument userDocument = new UserDocument(java.util.UUID.randomUUID());
    CreatedAt createdAt = CreatedAt.from(LocalDateTime.now());

    // When
    UserService userService =
        UserService.reconstitute(
            serviceProviderId, serviceTypeId, yearOfExperience, userDocument, createdAt);

    // Then
    assertThat(userService.getServiceProviderId()).isEqualTo(serviceProviderId);
    assertThat(userService.getServiceTypeId()).isEqualTo(serviceTypeId);
    assertThat(userService.getYearOfExperience()).isEqualTo(yearOfExperience);
    assertThat(userService.getUserDocument()).isEqualTo(userDocument);
    assertThat(userService.getCreatedAt()).isEqualTo(createdAt);
  }
}
