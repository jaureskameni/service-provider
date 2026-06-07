package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceProviderServiceTypeJpaRepositoryTest {

  @Mock private ServiceTypeSpringRepository serviceTypeSpringRepository;
  @Mock private JpaMapper jpaMapper;
  @InjectMocks private ServiceProviderServiceTypeJpaRepository objectUnderTest;

  @Test
  void loadAllAsView_shouldReturnMappedViews() {
    // Given
    ServiceTypeJpa jpa = new ServiceTypeJpa();
    ServiceTypeView view =
        new ServiceTypeView(java.util.UUID.randomUUID(), "name", "category", true);

    when(serviceTypeSpringRepository.findAll()).thenReturn(List.of(jpa));
    when(jpaMapper.toServiceTypeView(jpa)).thenReturn(view);

    // When
    List<ServiceTypeView> result = objectUnderTest.loadAllAsView();

    // Then
    assertThat(result).hasSize(1).contains(view);
    verify(serviceTypeSpringRepository).findAll();
    verify(jpaMapper).toServiceTypeView(jpa);
  }
}
