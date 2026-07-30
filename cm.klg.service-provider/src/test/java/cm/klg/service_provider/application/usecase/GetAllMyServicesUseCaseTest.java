package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceTypeViews;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.UserId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllMyServicesUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @InjectMocks private GetAllMyServicesUseCase objectUnderTest;

  @Test
  void execute_shouldReturnServiceList_whenServicesExistTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var views =
        List.of(
            new UserServiceView(
                new ServiceTypeViews.ServiceTypeView(
                    UUID.randomUUID(), "Plumbing", "repairs", true),
                5,
                UUID.randomUUID(),
                LocalDateTime.now()),
            new UserServiceView(
                new ServiceTypeViews.ServiceTypeView(
                    UUID.randomUUID(), "Electricity", "repairs", true),
                3,
                UUID.randomUUID(),
                LocalDateTime.now()));

    when(serviceProviderRepository.loadAllMyServices(userId)).thenReturn(views);

    // When
    var result = objectUnderTest.execute(userId);

    // Then
    assertThat(result).hasSize(2).isEqualTo(views);
    verify(serviceProviderRepository).loadAllMyServices(userId);
  }

  @Test
  void execute_shouldReturnEmptyList_whenNoServicesExistTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());

    when(serviceProviderRepository.loadAllMyServices(userId)).thenReturn(List.of());

    // When
    var result = objectUnderTest.execute(userId);

    // Then
    assertThat(result).isEmpty();
    verify(serviceProviderRepository).loadAllMyServices(userId);
  }

  @Test
  void execute_shouldCallRepositoryWithCorrectUserIdTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    when(serviceProviderRepository.loadAllMyServices(userId)).thenReturn(List.of());

    // When
    objectUnderTest.execute(userId);

    // Then
    verify(serviceProviderRepository).loadAllMyServices(userId);
  }

  @Test
  void execute_shouldReturnServiceViewsWithCorrectFieldsTest() {
    // Given
    var userId = UserId.from(UUID.randomUUID());
    var serviceTypeId = UUID.randomUUID();
    var yearOfExperience = 7;
    var document = UUID.randomUUID();
    var createdAt = LocalDateTime.now();

    var view =
        new UserServiceView(
            new ServiceTypeViews.ServiceTypeView(serviceTypeId, "Service Type", "category", true),
            yearOfExperience,
            document,
            createdAt);

    when(serviceProviderRepository.loadAllMyServices(userId)).thenReturn(List.of(view));

    // When
    var result = objectUnderTest.execute(userId);

    // Then
    assertThat(result).hasSize(1);
    UserServiceView returnedView = result.get(0);
    assertThat(returnedView.yearOfExperience()).isEqualTo(yearOfExperience);
    assertThat(returnedView.document()).isEqualTo(document);
    assertThat(returnedView.createdAt()).isEqualTo(createdAt);
  }
}
