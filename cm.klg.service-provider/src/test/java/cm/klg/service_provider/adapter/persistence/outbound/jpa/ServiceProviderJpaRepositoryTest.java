package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProvider;
import cm.klg.service_provider.domain.ServiceProvider.UserCityId;
import cm.klg.service_provider.domain.ServiceProvider.UserDistrictId;
import cm.klg.service_provider.domain.UserId;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceProviderJpaRepositoryTest {

  @Mock private ServiceProviderSpringRepository serviceProviderSpringRepository;
  @Mock private JpaMapper jpaMapper;
  @InjectMocks private ServiceProviderJpaRepository objectUnderTest;

  @Test
  void insert_shouldMapAndSaveServiceProvider() {
    // Given
    ServiceProvider serviceProvider =
        ServiceProvider.of(
            new UserId(UUID.randomUUID()),
            new UserCityId(UUID.randomUUID()),
            new UserDistrictId(UUID.randomUUID()),
            new PhoneNumber("+237", "678901234"),
            new ArrayList<>());

    ServiceProviderJpa serviceProviderJpa = new ServiceProviderJpa();
    serviceProviderJpa.setId(serviceProvider.getId().value());

    when(jpaMapper.toServiceProviderJpa(serviceProvider)).thenReturn(serviceProviderJpa);

    // When
    objectUnderTest.insert(serviceProvider);

    // Then
    verify(jpaMapper).toServiceProviderJpa(serviceProvider);
    verify(serviceProviderSpringRepository).save(serviceProviderJpa);
  }

  @Test
  void existsByUserId_shouldReturnTrue_whenUserExists() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    when(serviceProviderSpringRepository.existsByUserId(userId.value())).thenReturn(true);

    // When
    boolean exists = objectUnderTest.existsByUserId(userId);

    // Then
    assertThat(exists).isTrue();
    verify(serviceProviderSpringRepository).existsByUserId(userId.value());
  }

  @Test
  void existsByUserId_shouldReturnFalse_whenUserDoesNotExist() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    when(serviceProviderSpringRepository.existsByUserId(userId.value())).thenReturn(false);

    // When
    boolean exists = objectUnderTest.existsByUserId(userId);

    // Then
    assertThat(exists).isFalse();
    verify(serviceProviderSpringRepository).existsByUserId(userId.value());
  }
}
