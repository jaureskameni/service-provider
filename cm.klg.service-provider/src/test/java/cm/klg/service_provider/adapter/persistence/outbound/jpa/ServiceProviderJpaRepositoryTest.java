package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.ProviderAudit;
import cm.klg.service_provider.domain.service_provider.ProviderContact;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ProviderReview;
import cm.klg.service_provider.domain.service_provider.ServiceCollections;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ServiceProviderJpaRepositoryTest {

  @Mock private ServiceProviderSpringRepository serviceProviderSpringRepository;
  @Mock private UserSpringRepository userSpringRepository;
  @Mock private ServiceTypeSpringRepository serviceTypeSpringRepository;
  @Mock private JpaMapper jpaMapper;
  @InjectMocks private ServiceProviderJpaRepository objectUnderTest;

  @Test
  void insert_shouldMapAndSaveServiceProvider() {
    // Given
    ServiceProvider serviceProvider = createServiceProvider();
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
  void searchByLocationAndStatus_shouldCallSpringRepository() {
    // Given
    var serviceTypeId = new ServiceTypeId(UUID.randomUUID());
    var cityId = new UserCityId(UUID.randomUUID());
    var districtId = new UserDistrictId(UUID.randomUUID());
    var quarterId = new UserQuarterId(UUID.randomUUID());
    var pagination = new PaginationFetchRequest(10, 0);
    var pageable = PageRequest.of(0, 10);

    when(serviceProviderSpringRepository.searchIdsByLocationAndStatus(
            serviceTypeId.value(),
            cityId.value(),
            districtId.value(),
            quarterId.value(),
            "APPROVED",
            pageable))
        .thenReturn(new PageImpl<>(List.of()));

    // When
    var result =
        objectUnderTest.searchByLocationAndStatus(
            serviceTypeId,
            cityId,
            districtId,
            quarterId,
            ServiceProviderStatus.APPROVED,
            pagination);

    // Then
    assertThat(result).isNotNull();
    verify(serviceProviderSpringRepository)
        .searchIdsByLocationAndStatus(
            serviceTypeId.value(),
            cityId.value(),
            districtId.value(),
            quarterId.value(),
            "APPROVED",
            pageable);
  }

  @Test
  void load_shouldReturnServiceProvider_whenFound() {
    // Given
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    ServiceProviderJpa serviceProviderJpa = new ServiceProviderJpa();
    serviceProviderJpa.setId(serviceProviderId.value());

    ServiceProvider serviceProvider = createServiceProvider(serviceProviderId);

    when(serviceProviderSpringRepository.findAggregateById(serviceProviderId.value()))
        .thenReturn(java.util.Optional.of(serviceProviderJpa));
    when(jpaMapper.toServiceProviderDomain(serviceProviderJpa)).thenReturn(serviceProvider);

    // When
    ServiceProvider result = objectUnderTest.load(serviceProviderId);

    // Then
    assertThat(result).isEqualTo(serviceProvider);
  }

  @Test
  void loadAllPortfolio_shouldReturnMyPortfolioViews_whenItemsExist() {
    var userId = UserId.from(UUID.randomUUID());
    var spJpa = new ServiceProviderJpa();
    spJpa.setId(UUID.randomUUID());
    PortfolioItemJpa pi1 = new PortfolioItemJpa();
    pi1.setId(UUID.randomUUID());
    PortfolioItemJpa pi2 = new PortfolioItemJpa();
    pi2.setId(UUID.randomUUID());
    spJpa.setPortfolioItems(List.of(pi1, pi2));

    PortfolioView view1 = mock(PortfolioView.class);
    PortfolioView view2 = mock(PortfolioView.class);

    when(serviceProviderSpringRepository.findPortfolioItemsByUserId(userId.value()))
        .thenReturn(spJpa.getPortfolioItems());
    when(jpaMapper.toPortfolioView(pi1)).thenReturn(view1);
    when(jpaMapper.toPortfolioView(pi2)).thenReturn(view2);

    var result = objectUnderTest.loadAllMyPortfolio(userId);

    assertThat(result).hasSize(2).containsExactly(view1, view2);
    verify(serviceProviderSpringRepository).findPortfolioItemsByUserId(userId.value());
    verify(jpaMapper).toPortfolioView(pi1);
    verify(jpaMapper).toPortfolioView(pi2);
  }

  @Test
  void loadAllMyPortfolio_shouldReturnEmptyList_whenNoItems() {
    var userId = UserId.from(UUID.randomUUID());

    when(serviceProviderSpringRepository.findPortfolioItemsByUserId(userId.value()))
        .thenReturn(List.of());

    var result = objectUnderTest.loadAllMyPortfolio(userId);

    assertThat(result).isEmpty();
    verify(serviceProviderSpringRepository).findPortfolioItemsByUserId(userId.value());
    verifyNoInteractions(jpaMapper);
  }

  @Test
  void loadAllProviderPortfolio_shouldReturnPortfolioViews_whenItemsExist() {
    var providerId = ServiceProviderId.from(UUID.randomUUID());
    var spJpa = new ServiceProviderJpa();
    spJpa.setId(providerId.value());
    PortfolioItemJpa pi1 = new PortfolioItemJpa();
    pi1.setId(UUID.randomUUID());
    PortfolioItemJpa pi2 = new PortfolioItemJpa();
    pi2.setId(UUID.randomUUID());
    spJpa.setPortfolioItems(List.of(pi1, pi2));

    PortfolioView view1 = mock(PortfolioView.class);
    PortfolioView view2 = mock(PortfolioView.class);

    when(serviceProviderSpringRepository.findPortfolioItemsByProviderId(providerId.value()))
        .thenReturn(spJpa.getPortfolioItems());
    when(jpaMapper.toPortfolioView(pi1)).thenReturn(view1);
    when(jpaMapper.toPortfolioView(pi2)).thenReturn(view2);

    var result = objectUnderTest.loadAllProviderPortfolio(providerId);

    assertThat(result).hasSize(2).containsExactly(view1, view2);
    verify(serviceProviderSpringRepository).findPortfolioItemsByProviderId(providerId.value());
    verify(jpaMapper).toPortfolioView(pi1);
    verify(jpaMapper).toPortfolioView(pi2);
  }

  @Test
  void loadAllProviderPortfolio_shouldReturnEmptyList_whenNoItems() {
    var providerId = ServiceProviderId.from(UUID.randomUUID());

    when(serviceProviderSpringRepository.findPortfolioItemsByProviderId(providerId.value()))
        .thenReturn(List.of());

    var result = objectUnderTest.loadAllProviderPortfolio(providerId);

    assertThat(result).isEmpty();
    verify(serviceProviderSpringRepository).findPortfolioItemsByProviderId(providerId.value());
    verifyNoInteractions(jpaMapper);
  }

  @Test
  void loadPublicProfile_shouldFetchUserAndServiceTypesAndMapToViewTest() {
    // Given
    UUID spId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    UUID stId = UUID.randomUUID();

    ServiceProviderJpa spJpa = new ServiceProviderJpa();
    spJpa.setId(spId);
    spJpa.setUserId(userId);
    UserServiceJpa usJpa = new UserServiceJpa();
    UserServiceJpaId usId = new UserServiceJpaId();
    usId.setServiceTypeId(stId);
    usJpa.setId(usId);
    spJpa.setUserServices(List.of(usJpa));

    UserJpa userJpa = new UserJpa();
    userJpa.setId(userId);

    ServiceTypeJpa stJpa = new ServiceTypeJpa();
    stJpa.setId(stId);

    ServiceProviderView view = mock(ServiceProviderView.class);

    when(serviceProviderSpringRepository.findAggregateByIdAndStatus(
            spId, ServiceProviderStatus.APPROVED.name()))
        .thenReturn(Optional.of(spJpa));
    when(userSpringRepository.findByIdentityId(userId)).thenReturn(Optional.of(userJpa));
    when(serviceTypeSpringRepository.findAllById(anyList())).thenReturn(List.of(stJpa));
    when(jpaMapper.toServiceProviderView(spJpa, userJpa, List.of(stJpa))).thenReturn(view);

    // When
    var result = objectUnderTest.loadProfile(new ServiceProviderId(spId));

    // Then
    assertThat(result).isEqualTo(view);
    verify(userSpringRepository).findByIdentityId(userId);
    verify(serviceTypeSpringRepository).findAllById(List.of(stId));
  }

  private ServiceProvider createServiceProvider() {
    return createServiceProvider(ServiceProviderId.generate());
  }

  private ServiceProvider createServiceProvider(ServiceProviderId id) {
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            id,
            new UserId(UUID.randomUUID()),
            new ProviderContact(
                new ProviderLocation(
                    new UserCityId(UUID.randomUUID()),
                    new UserDistrictId(UUID.randomUUID()),
                    new UserQuarterId(UUID.randomUUID())),
                new PhoneNumber("+237", "678901234")),
            new ProviderReview(ServiceProviderStatus.PENDING, null, null, null),
            new ProviderAudit(cm.klg.common.base.domain.CreatedAt.from(LocalDateTime.now()), null),
            null,
            new ServiceCollections(new ArrayList<>(), new ArrayList<>()));
    serviceProvider.addUserService(
        new ServiceTypeId(UUID.randomUUID()),
        new YearOfExperience(1),
        new UserDocument(UUID.randomUUID()));
    return serviceProvider;
  }
}
