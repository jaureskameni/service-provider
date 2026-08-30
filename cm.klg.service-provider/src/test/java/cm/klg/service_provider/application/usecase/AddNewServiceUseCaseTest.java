package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.service_type.ServiceTypeNotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddNewServiceUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private ServiceTypeRepository serviceTypeRepository;
  @Mock private DomainEventPublisher domainEventPublisher;

  @InjectMocks private AddNewServiceUseCase objectUnderTest;

  @Test
  void execute_shouldLoadAddServiceAndUpdateServiceProvider() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    ServiceTypeId serviceTypeId = new ServiceTypeId(UUID.randomUUID());
    YearOfExperience yearOfExperience = new YearOfExperience(5);
    UserDocument userDocument = new UserDocument(UUID.randomUUID());

    AddNewServiceUseCase.AddNewServiceCommand command =
        new AddNewServiceUseCase.AddNewServiceCommand(
            userId, serviceTypeId, yearOfExperience, userDocument);

    ServiceProvider serviceProvider =
        ServiceProvider.of(
            userId,
            new ProviderLocation(
                new UserCityId(UUID.randomUUID()),
                new UserDistrictId(UUID.randomUUID()),
                new UserQuarterId(UUID.randomUUID())),
            new PhoneNumber("+237", "678901234"),
            null,
            new ArrayList<>());

    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);
    when(serviceTypeRepository.existsById(serviceTypeId)).thenReturn(true);

    // When
    objectUnderTest.execute(command);

    // Then
    assertThat(serviceProvider.getUserServices()).hasSize(1);
    assertThat(serviceProvider.getUserServices().get(0).getServiceTypeId())
        .isEqualTo(serviceTypeId);
    assertThat(serviceProvider.getUserServices().get(0).getYearOfExperience())
        .isEqualTo(yearOfExperience);
    assertThat(serviceProvider.getUserServices().get(0).getUserDocument()).isEqualTo(userDocument);
    verify(serviceProviderRepository).loadByUserId(userId);
    verify(serviceTypeRepository).existsById(serviceTypeId);
    verify(serviceProviderRepository).update(serviceProvider);
  }

  @Test
  void execute_shouldRejectUnknownServiceTypeTest() {
    // Given
    ServiceTypeId serviceTypeId = new ServiceTypeId(UUID.randomUUID());
    AddNewServiceUseCase.AddNewServiceCommand command =
        new AddNewServiceUseCase.AddNewServiceCommand(
            new UserId(UUID.randomUUID()),
            serviceTypeId,
            new YearOfExperience(5),
            new UserDocument(UUID.randomUUID()));
    when(serviceTypeRepository.existsById(serviceTypeId)).thenReturn(false);

    // When & Then
    assertThatThrownBy(() -> objectUnderTest.execute(command))
        .isInstanceOf(ServiceTypeNotFoundException.class);
    verify(serviceProviderRepository, never()).loadByUserId(command.userId());
  }
}
