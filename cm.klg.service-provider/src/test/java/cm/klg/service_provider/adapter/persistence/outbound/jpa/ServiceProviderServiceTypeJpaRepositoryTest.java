package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView1;
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
  void loadAllAsView1_shouldReturnMappedViews() {
    // Given
    ServiceTypeJpa jpa = new ServiceTypeJpa();
    ServiceTypeView1 view = mock(ServiceTypeView1.class);

    when(serviceTypeSpringRepository.findAll()).thenReturn(List.of(jpa));
    when(jpaMapper.toServiceTypeView1(jpa)).thenReturn(view);

    // When
    List<ServiceTypeView1> result = objectUnderTest.loadAllAsView1();

    // Then
    assertThat(result).hasSize(1).contains(view);
    verify(serviceTypeSpringRepository).findAll();
    verify(jpaMapper).toServiceTypeView1(jpa);
  }
}
