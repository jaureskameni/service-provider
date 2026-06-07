package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllServiceTypesUseCaseTest {

  @Mock private ServiceTypeRepository serviceTypeRepository;
  @Mock private ServiceTypeView serviceTypeView;
  @InjectMocks private GetAllServiceTypesUseCase objectUnderTest;

  @Test
  void execute_shouldReturnAllServiceTypeViews() {
    // Given
    List<ServiceTypeView> views = List.of(serviceTypeView);
    when(serviceTypeRepository.loadAllAsView()).thenReturn(views);

    // When
    var result = objectUnderTest.execute();

    // Then
    assertThat(result).isEqualTo(views);
    verify(serviceTypeRepository).loadAllAsView();
  }
}
