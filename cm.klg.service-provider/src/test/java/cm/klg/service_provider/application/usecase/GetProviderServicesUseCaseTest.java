package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceTypeViews;
import cm.klg.service_provider.application.views.UserServiceView;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetProviderServicesUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @InjectMocks private GetProviderServicesUseCase objectUnderTest;

  @Test
  void execute_shouldReturnServiceList_whenServicesExistTest() {
    // Given
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());
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

    when(serviceProviderRepository.loadAllApprovedProviderServices(serviceProviderId))
        .thenReturn(views);

    // When
    var result = objectUnderTest.execute(serviceProviderId);

    // Then
    assertThat(result).hasSize(2).isEqualTo(views);
    verify(serviceProviderRepository).loadAllApprovedProviderServices(serviceProviderId);
  }

  @Test
  void execute_shouldReturnEmptyList_whenNoServicesExistTest() {
    // Given
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());

    when(serviceProviderRepository.loadAllApprovedProviderServices(serviceProviderId))
        .thenReturn(List.of());

    // When
    var result = objectUnderTest.execute(serviceProviderId);

    // Then
    assertThat(result).isEmpty();
    verify(serviceProviderRepository).loadAllApprovedProviderServices(serviceProviderId);
  }

  @Test
  void execute_shouldCallRepositoryWithCorrectServiceProviderIdTest() {
    // Given
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    when(serviceProviderRepository.loadAllApprovedProviderServices(serviceProviderId))
        .thenReturn(List.of());

    // When
    objectUnderTest.execute(serviceProviderId);

    // Then
    verify(serviceProviderRepository).loadAllApprovedProviderServices(serviceProviderId);
  }

  @Test
  void execute_shouldReturnServiceViewsWithCorrectFieldsTest() {
    // Given
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    var serviceTypeId = UUID.randomUUID();
    var yearOfExperience = 10;
    var document = UUID.randomUUID();
    var createdAt = LocalDateTime.now();

    var view =
        new UserServiceView(
            new ServiceTypeViews.ServiceTypeView(serviceTypeId, "Carpentry", "construction", true),
            yearOfExperience,
            document,
            createdAt);

    when(serviceProviderRepository.loadAllApprovedProviderServices(serviceProviderId))
        .thenReturn(List.of(view));

    // When
    var result = objectUnderTest.execute(serviceProviderId);

    // Then
    assertThat(result).hasSize(1);
    UserServiceView returnedView = result.getFirst();
    assertThat(returnedView.yearOfExperience()).isEqualTo(yearOfExperience);
    assertThat(returnedView.document()).isEqualTo(document);
    assertThat(returnedView.createdAt()).isEqualTo(createdAt);
  }

  @Test
  void execute_shouldReturnPublicServiceInformationTest() {
    // Given - Verify that only public services are returned
    var serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    var publicView =
        new UserServiceView(
            new ServiceTypeViews.ServiceTypeView(
                UUID.randomUUID(), "Public Service", "category", true),
            5,
            UUID.randomUUID(),
            LocalDateTime.now());

    when(serviceProviderRepository.loadAllApprovedProviderServices(serviceProviderId))
        .thenReturn(List.of(publicView));

    // When
    var result = objectUnderTest.execute(serviceProviderId);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.getFirst()).isEqualTo(publicView);
  }
}
