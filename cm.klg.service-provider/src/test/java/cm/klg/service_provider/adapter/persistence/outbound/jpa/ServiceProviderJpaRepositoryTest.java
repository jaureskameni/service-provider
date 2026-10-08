package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderIdentityView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderVerificationView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView3;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.CniRectoMediaId;
import cm.klg.service_provider.domain.service_provider.CniVersoMediaId;
import cm.klg.service_provider.domain.service_provider.IdentityDocuments;
import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderStatusException;
import cm.klg.service_provider.domain.service_provider.ProfileImageMediaId;
import cm.klg.service_provider.domain.service_provider.ProfileImageReview;
import cm.klg.service_provider.domain.service_provider.ProfileImageReviewStatus;
import cm.klg.service_provider.domain.service_provider.ProviderAudit;
import cm.klg.service_provider.domain.service_provider.ProviderContact;
import cm.klg.service_provider.domain.service_provider.ProviderImages;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ProviderProfile;
import cm.klg.service_provider.domain.service_provider.ProviderReview;
import cm.klg.service_provider.domain.service_provider.ServiceCollections;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderState;
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
    verify(serviceProviderSpringRepository).saveAndFlush(serviceProviderJpa);
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
    when(serviceProviderSpringRepository.findAggregateWithPortfolioById(serviceProviderId.value()))
        .thenReturn(java.util.Optional.of(serviceProviderJpa));
    when(jpaMapper.toServiceProviderDomain(serviceProviderJpa)).thenReturn(serviceProvider);

    // When
    ServiceProvider result = objectUnderTest.load(serviceProviderId);

    // Then
    assertThat(result).isEqualTo(serviceProvider);
    verify(serviceProviderSpringRepository).findAggregateById(serviceProviderId.value());
    verify(serviceProviderSpringRepository)
        .findAggregateWithPortfolioById(serviceProviderId.value());
  }

  @Test
  void loadPendingProfileImageReviewAsView3_shouldMapProviderAndUser() {
    var providerId = ServiceProviderId.generate();
    var userId = UUID.randomUUID();
    var providerJpa = new ServiceProviderJpa();
    providerJpa.setId(providerId.value());
    providerJpa.setUserId(userId);
    providerJpa.setStatus(ServiceProviderStatus.APPROVED.name());
    providerJpa.setProfileImageReviewStatus(ProfileImageReviewStatus.PENDING_REVIEW.name());
    var userJpa = new UserJpa();
    userJpa.setId(userId);
    var expectedView = mock(ServiceProviderView3.class);
    var services = List.<UserServiceView>of();

    when(serviceProviderSpringRepository.findForProfileImageReviewById(providerId.value()))
        .thenReturn(Optional.of(providerJpa));
    when(userSpringRepository.findById(userId)).thenReturn(Optional.of(userJpa));
    when(serviceProviderSpringRepository.findUserServicesByServiceProviderId(providerId.value()))
        .thenReturn(List.of());
    when(serviceTypeSpringRepository.findAllById(List.of())).thenReturn(List.of());
    when(jpaMapper.toUserServiceViews(List.of(), List.of())).thenReturn(services);
    when(jpaMapper.toServiceProviderView3(providerJpa, userJpa, services)).thenReturn(expectedView);

    var result = objectUnderTest.loadPendingProfileImageReviewAsView3(providerId);

    assertThat(result).isSameAs(expectedView);
    verify(serviceProviderSpringRepository).findForProfileImageReviewById(providerId.value());
    verify(jpaMapper).toServiceProviderView3(providerJpa, userJpa, services);
  }

  @Test
  void loadPendingProfileImageReviewAsView3_shouldRejectNonPendingReview() {
    var providerId = ServiceProviderId.generate();
    var providerJpa = new ServiceProviderJpa();
    providerJpa.setId(providerId.value());
    providerJpa.setStatus(ServiceProviderStatus.APPROVED.name());
    providerJpa.setProfileImageReviewStatus(ProfileImageReviewStatus.NONE.name());
    when(serviceProviderSpringRepository.findForProfileImageReviewById(providerId.value()))
        .thenReturn(Optional.of(providerJpa));

    assertThatThrownBy(() -> objectUnderTest.loadPendingProfileImageReviewAsView3(providerId))
        .isInstanceOf(InvalidServiceProviderStatusException.class);

    verifyNoInteractions(userSpringRepository, jpaMapper);
  }

  @Test
  void loadForVerification_shouldLoadProviderAndIdentityRelations() {
    var providerId = ServiceProviderId.generate();
    var userId = UUID.randomUUID();
    var providerJpa = new ServiceProviderJpa();
    providerJpa.setId(providerId.value());
    providerJpa.setUserId(userId);
    var userJpa = new UserJpa();
    userJpa.setId(userId);
    var services = List.<UserServiceView>of();
    var expectedView = mock(ServiceProviderVerificationView.class);

    when(serviceProviderSpringRepository.findForVerificationById(providerId.value()))
        .thenReturn(Optional.of(providerJpa));
    when(userSpringRepository.findById(userId)).thenReturn(Optional.of(userJpa));
    when(serviceProviderSpringRepository.findUserServicesByServiceProviderId(providerId.value()))
        .thenReturn(List.of());
    when(serviceTypeSpringRepository.findAllById(List.of())).thenReturn(List.of());
    when(jpaMapper.toUserServiceViews(List.of(), List.of())).thenReturn(services);
    when(serviceProviderSpringRepository.findPortfolioItemsByServiceProviderId(providerId.value()))
        .thenReturn(List.of());
    when(jpaMapper.toServiceProviderVerificationView(providerJpa, userJpa, services, List.of()))
        .thenReturn(expectedView);

    var result = objectUnderTest.loadForVerification(providerId);

    assertThat(result).isSameAs(expectedView);
    verify(serviceProviderSpringRepository).findForVerificationById(providerId.value());
    verify(jpaMapper).toServiceProviderVerificationView(providerJpa, userJpa, services, List.of());
  }

  @Test
  void loadIdentityVerificationByUserId_shouldReturnMappedIdentityView() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var providerJpa = mock(ServiceProviderJpa.class);
    var serviceProviderIdentityView = mock(ServiceProviderIdentityView.class);

    when(serviceProviderSpringRepository.findIdentityVerificationByUserId(userId.value()))
        .thenReturn(Optional.of(providerJpa));
    when(jpaMapper.toServiceProviderIdentityView(providerJpa))
        .thenReturn(serviceProviderIdentityView);

    // When
    ServiceProviderIdentityView result = objectUnderTest.loadIdentityVerificationByUserId(userId);

    // Then
    assertThat(result).isEqualTo(serviceProviderIdentityView);
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
  void loadByUserId_shouldReturnServiceProvider_whenFound() {
    // Given
    UUID userId = UUID.randomUUID();
    ServiceProviderJpa spJpa = new ServiceProviderJpa();
    spJpa.setId(UUID.randomUUID());
    spJpa.setUserId(userId);

    ServiceProvider domainSp = createServiceProvider();
    when(serviceProviderSpringRepository.findAggregateByUserId(userId))
        .thenReturn(Optional.of(spJpa));
    when(serviceProviderSpringRepository.findAggregateWithPortfolioByUserId(userId))
        .thenReturn(Optional.of(spJpa));
    when(jpaMapper.toServiceProviderDomain(spJpa)).thenReturn(domainSp);

    // When
    var result = objectUnderTest.loadByUserId(new UserId(userId));

    // Then
    assertThat(result).isEqualTo(domainSp);
    verify(serviceProviderSpringRepository).findAggregateByUserId(userId);
    verify(serviceProviderSpringRepository).findAggregateWithPortfolioByUserId(userId);
    verify(jpaMapper).toServiceProviderDomain(spJpa);
  }

  private ServiceProvider createServiceProvider() {
    return createServiceProvider(ServiceProviderId.generate());
  }

  private ServiceProvider createServiceProvider(ServiceProviderId id) {
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            id,
            new UserId(UUID.randomUUID()),
            ServiceProviderState.from(
                new ProviderProfile(
                    new ProviderContact(
                        new ProviderLocation(
                            new UserCityId(UUID.randomUUID()),
                            new UserDistrictId(UUID.randomUUID()),
                            new UserQuarterId(UUID.randomUUID())),
                        new PhoneNumber("+237", "678901234")),
                    null),
                new ProviderReview(ServiceProviderStatus.PENDING, null, null, null),
                new ProviderAudit(
                    cm.klg.common.base.domain.CreatedAt.from(LocalDateTime.now()), null),
                IdentityDocuments.of(
                    CniRectoMediaId.from(UUID.randomUUID()),
                    CniVersoMediaId.from(UUID.randomUUID())),
                ProviderImages.from(
                    ProfileImageMediaId.from(UUID.randomUUID()), ProfileImageReview.none()),
                new ServiceCollections(new ArrayList<>(), new ArrayList<>())));
    serviceProvider.addUserService(
        new ServiceTypeId(UUID.randomUUID()),
        new YearOfExperience(1),
        new UserDocument(UUID.randomUUID()));
    return serviceProvider;
  }
}
