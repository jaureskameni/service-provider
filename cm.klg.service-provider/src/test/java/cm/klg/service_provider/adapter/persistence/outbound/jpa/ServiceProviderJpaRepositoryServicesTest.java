package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceProviderJpaRepositoryServicesTest {
  @Mock private ServiceProviderSpringRepository serviceProviderSpringRepository;
  @Mock private UserSpringRepository userSpringRepository;
  @Mock private ServiceTypeSpringRepository serviceTypeSpringRepository;
  @Mock private JpaMapper jpaMapper;
  @InjectMocks private ServiceProviderJpaRepository objectUnderTest;

  @Test
  void loadAllMyServices_shouldResolveTheProviderThenMapItsServices() {
    var userId = UserId.from(UUID.randomUUID());
    var providerId = ServiceProviderId.from(UUID.randomUUID());
    var providerJpa = new ServiceProviderJpa();
    var provider = org.mockito.Mockito.mock(ServiceProvider.class);
    var userServiceJpa = new UserServiceJpa();
    userServiceJpa.setId(userServiceJpaId());
    var view = serviceView();
    when(serviceProviderSpringRepository.findAggregateByUserId(userId.value()))
        .thenReturn(Optional.of(providerJpa));
    when(serviceProviderSpringRepository.findAggregateWithPortfolioByUserId(userId.value()))
        .thenReturn(Optional.of(providerJpa));
    when(jpaMapper.toServiceProviderDomain(providerJpa)).thenReturn(provider);
    when(provider.getId()).thenReturn(providerId);
    when(serviceProviderSpringRepository.findUserServicesByServiceProviderId(providerId.value()))
        .thenReturn(List.of(userServiceJpa));
    when(jpaMapper.toUserServiceViews(List.of(userServiceJpa), List.of()))
        .thenReturn(List.of(view));

    var result = objectUnderTest.loadAllMyServices(userId);

    assertThat(result).containsExactly(view);
    verify(serviceProviderSpringRepository).findAggregateByUserId(userId.value());
    verify(serviceProviderSpringRepository).findAggregateWithPortfolioByUserId(userId.value());
    verify(serviceProviderSpringRepository).findUserServicesByServiceProviderId(providerId.value());
  }

  private UserServiceView serviceView() {
    return new UserServiceView(
        new ServiceTypeView(UUID.randomUUID(), "Plumbing", "Repairs", true),
        5,
        UUID.randomUUID(),
        LocalDateTime.now());
  }

  private UserServiceJpaId userServiceJpaId() {
    var id = new UserServiceJpaId();
    id.setServiceProviderId(UUID.randomUUID());
    id.setServiceTypeId(UUID.randomUUID());
    return id;
  }
}
