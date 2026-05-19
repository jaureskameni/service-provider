package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
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
import cm.klg.service_provider.utils.PaginationFetchRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

  @Test
  void loadAll_shouldReturnAllAsView1ServiceProviders_whenStatusIsNull() {
    // Given
    ServiceProviderJpa serviceProviderJpa = serviceProviderJpa(ServiceProviderStatus.PENDING);
    ServiceProviderView1 serviceProviderView = Mockito.mock(ServiceProviderView1.class);
    var pageable = PageRequest.of(0, 10);
    when(serviceProviderSpringRepository.findAllAggregate(pageable))
        .thenReturn(new PageImpl<>(List.of(serviceProviderJpa), pageable, 1));
    when(jpaMapper.toServiceProviderView1(serviceProviderJpa)).thenReturn(serviceProviderView);

    // When
    var result = objectUnderTest.loadAllAsView1(new PaginationFetchRequest(10, 0));

    // Then
    assertThat(result.total()).isEqualTo(1);
    assertThat(result.elements()).containsExactly(serviceProviderView);
    verify(serviceProviderSpringRepository).findAllAggregate(pageable);
    verify(jpaMapper).toServiceProviderView1(serviceProviderJpa);
  }

  @Test
  void loadAllByStatusAsView1_shouldReturnFilteredServiceProviders_whenStatusIsProvided() {
    // Given
    ServiceProviderJpa serviceProviderJpa = serviceProviderJpa(ServiceProviderStatus.APPROVED);
    ServiceProviderView1 serviceProviderView = Mockito.mock(ServiceProviderView1.class);
    var pageable = PageRequest.of(1, 5);
    when(serviceProviderSpringRepository.findAllAggregateByStatus("APPROVED", pageable))
        .thenReturn(new PageImpl<>(List.of(serviceProviderJpa), pageable, 12));
    when(jpaMapper.toServiceProviderView1(serviceProviderJpa)).thenReturn(serviceProviderView);

    // When
    var result =
        objectUnderTest.loadAllByStatusAsView1(
            ServiceProviderStatus.APPROVED, new PaginationFetchRequest(5, 1));

    // Then
    assertThat(result.total()).isEqualTo(12);
    assertThat(result.elements()).containsExactly(serviceProviderView);
    verify(serviceProviderSpringRepository).findAllAggregateByStatus("APPROVED", pageable);
    verify(jpaMapper).toServiceProviderView1(serviceProviderJpa);
  }

  @Test
  void loadAllByStatusAsView1_shouldReturnEmptyPage_whenNoServiceProviderFound() {
    // Given
    var pageable = PageRequest.of(0, 10);
    when(serviceProviderSpringRepository.findAllAggregateByStatus("REJECTED", pageable))
        .thenReturn(new PageImpl<>(List.of(), pageable, 0));

    // When
    var result =
        objectUnderTest.loadAllByStatusAsView1(
            ServiceProviderStatus.REJECTED, new PaginationFetchRequest(10, 0));

    // Then
    assertThat(result.total()).isZero();
    assertThat(result.elements()).isEmpty();
    verify(serviceProviderSpringRepository).findAllAggregateByStatus("REJECTED", pageable);
  }

  private ServiceProviderJpa serviceProviderJpa(ServiceProviderStatus status) {
    UUID serviceProviderId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();

    UserServiceJpaId userServiceJpaId = new UserServiceJpaId();
    userServiceJpaId.setServiceProviderId(serviceProviderId);
    userServiceJpaId.setServiceTypeId(UUID.randomUUID());

    UserServiceJpa userServiceJpa = new UserServiceJpa();
    userServiceJpa.setId(userServiceJpaId);
    userServiceJpa.setYearOfExperience(5);
    userServiceJpa.setUserDocument(UUID.randomUUID());
    userServiceJpa.setCreatedAt(createdAt);

    ServiceProviderJpa serviceProviderJpa = new ServiceProviderJpa();
    serviceProviderJpa.setId(serviceProviderId);
    serviceProviderJpa.setUserId(UUID.randomUUID());
    serviceProviderJpa.setCity(UUID.randomUUID());
    serviceProviderJpa.setDistrict(UUID.randomUUID());
    serviceProviderJpa.setPhoneNumber(new PhoneNumberJpa("+237", "678901234"));
    serviceProviderJpa.setStatus(status.name());
    serviceProviderJpa.setCreatedAt(createdAt);
    serviceProviderJpa.setUpdatedAt(createdAt.plusDays(1));
    serviceProviderJpa.setUserServices(List.of(userServiceJpa));
    return serviceProviderJpa;
  }
}
