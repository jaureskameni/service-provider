package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase.BecomeServiceProviderCommand;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BecomeServiceProviderUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
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
            userId, location, phoneNumber, serviceTypeId, yearOfExperience, document);

    ServiceProviderId expectedServiceProviderId = new ServiceProviderId(UUID.randomUUID());
    ServiceProvider mockServiceProvider = mock(ServiceProvider.class);

    try (MockedStatic<ServiceProvider> mockedStatic = mockStatic(ServiceProvider.class)) {
      mockedStatic
          .when(() -> ServiceProvider.of(userId, location, phoneNumber, new ArrayList<>()))
          .thenReturn(mockServiceProvider);
      when(mockServiceProvider.getId()).thenReturn(expectedServiceProviderId);

      // When
      ServiceProviderId result = objectUnderTest.execute(command);

      // Then
      assertThat(result).isEqualTo(expectedServiceProviderId);
      mockedStatic.verify(
          () -> ServiceProvider.of(userId, location, phoneNumber, new ArrayList<>()));
      verify(mockServiceProvider).addUserService(serviceTypeId, yearOfExperience, document);
      verify(serviceProviderRepository).insert(mockServiceProvider);
    }
  }
}
