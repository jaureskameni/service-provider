package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderWithPhoneNumberAlreadyExistsException;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderCreatedEvent;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BecomeServiceProviderUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private DomainEventPublisher domainEventPublisher;
  @InjectMocks private BecomeServiceProviderUseCase objectUnderTest;

  @Test
  void execute_shouldCreateAndSaveServiceProvider() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    ProviderLocation location =
        new ProviderLocation(
            new UserCityId(UUID.randomUUID()),
            new UserDistrictId(UUID.randomUUID()),
            new UserQuarterId(UUID.randomUUID()));
    PhoneNumber phoneNumber = new PhoneNumber("+237", "678901234");
    ServiceTypeId serviceTypeId = new ServiceTypeId(UUID.randomUUID());
    YearOfExperience yearOfExperience = new YearOfExperience(5);
    UserDocument document = new UserDocument(UUID.randomUUID());

    BecomeServiceProviderCommand command =
        new BecomeServiceProviderCommand(
            userId, location, phoneNumber, null, serviceTypeId, yearOfExperience, document);

    ServiceProviderId expectedServiceProviderId = new ServiceProviderId(UUID.randomUUID());
    ServiceProvider mockServiceProvider = mock(ServiceProvider.class);
    ServiceProviderCreatedEvent event = mock(ServiceProviderCreatedEvent.class);

    try (MockedStatic<ServiceProvider> mockedStatic = mockStatic(ServiceProvider.class)) {
      mockedStatic
          .when(() -> ServiceProvider.of(userId, location, phoneNumber, null, new ArrayList<>()))
          .thenReturn(mockServiceProvider);
      when(mockServiceProvider.getId()).thenReturn(expectedServiceProviderId);
      when(mockServiceProvider.toCreatedEvent()).thenReturn(event);

      // When
      ServiceProviderId result = objectUnderTest.execute(command);

      // Then
      assertThat(result).isEqualTo(expectedServiceProviderId);
      mockedStatic.verify(
          () -> ServiceProvider.of(userId, location, phoneNumber, null, new ArrayList<>()));
      verify(mockServiceProvider).addUserService(serviceTypeId, yearOfExperience, document);
      verify(serviceProviderRepository).insert(mockServiceProvider);
      verify(domainEventPublisher).serviceProviderCreatedEvent(event);
    }
  }

  @Test
  void execute_shouldThrowException_whenServiceProviderAlreadyExistsForUser() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    BecomeServiceProviderCommand command =
        new BecomeServiceProviderCommand(
            userId,
            mock(ProviderLocation.class),
            new PhoneNumber("+237", "678901234"),
            null,
            new ServiceTypeId(UUID.randomUUID()),
            new YearOfExperience(5),
            new UserDocument(UUID.randomUUID()));

    when(serviceProviderRepository.existsByUserId(userId)).thenReturn(true);

    // When & Then
    Assertions.assertThrows(
        ServiceProviderAlreadyExistsException.class, () -> objectUnderTest.execute(command));
  }

  @Test
  void execute_shouldThrowException_whenPhoneNumberAlreadyExists() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    PhoneNumber phoneNumber = new PhoneNumber("+237", "678901234");
    BecomeServiceProviderCommand command =
        new BecomeServiceProviderCommand(
            userId,
            mock(ProviderLocation.class),
            phoneNumber,
            null,
            new ServiceTypeId(UUID.randomUUID()),
            new YearOfExperience(5),
            new UserDocument(UUID.randomUUID()));

    when(serviceProviderRepository.existsByUserId(userId)).thenReturn(false);
    when(serviceProviderRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);

    // When & Then
    Assertions.assertThrows(
        ServiceProviderWithPhoneNumberAlreadyExistsException.class,
        () -> objectUnderTest.execute(command));
  }
}
