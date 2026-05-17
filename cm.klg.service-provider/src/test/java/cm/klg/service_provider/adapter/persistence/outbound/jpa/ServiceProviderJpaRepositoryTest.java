package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.time.LocalDateTime;
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

  @Test
  void load_shouldReturnServiceProvider_whenFound() {
    // Given
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    ServiceProviderJpa serviceProviderJpa = new ServiceProviderJpa();
    serviceProviderJpa.setId(serviceProviderId.value());
    serviceProviderJpa.setStatus("PENDING");

    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            serviceProviderId,
            new UserId(UUID.randomUUID()),
            new ProviderContact(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new PhoneNumber("+237", "678901234")),
            new ProviderReview(ServiceProviderStatus.PENDING, null, null),
            new ProviderAudit(cm.klg.common.base.domain.CreatedAt.from(LocalDateTime.now()), null),
            new ArrayList<>());

    when(serviceProviderSpringRepository.findAggregateById(serviceProviderId.value()))
        .thenReturn(java.util.Optional.of(serviceProviderJpa));
    when(jpaMapper.toServiceProviderDomain(serviceProviderJpa)).thenReturn(serviceProvider);

    // When
    ServiceProvider result = objectUnderTest.load(serviceProviderId);

    // Then
    assertThat(result).isEqualTo(serviceProvider);
    verify(serviceProviderSpringRepository).findAggregateById(serviceProviderId.value());
    verify(jpaMapper).toServiceProviderDomain(serviceProviderJpa);
  }

  @Test
  void update_shouldMapAndSaveServiceProvider_whenFound() {
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

    when(serviceProviderSpringRepository.findAggregateById(serviceProvider.getId().value()))
        .thenReturn(java.util.Optional.of(serviceProviderJpa));

    // When
    objectUnderTest.update(serviceProvider);

    // Then
    verify(serviceProviderSpringRepository).findAggregateById(serviceProvider.getId().value());
    verify(jpaMapper).toServiceProviderJpa(serviceProviderJpa, serviceProvider);
    verify(serviceProviderSpringRepository).save(serviceProviderJpa);
  }
}
