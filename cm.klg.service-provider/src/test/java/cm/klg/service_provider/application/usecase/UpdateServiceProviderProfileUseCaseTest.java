package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateServiceProviderProfileUseCaseTest {
  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private DomainEventPublisher domainEventPublisher;
  @InjectMocks private UpdateServiceProviderProfileUseCase objectUnderTest;

  @Test
  void execute_shouldUpdateTheAuthenticatedProvidersProfileTest() {
    var userId = UserId.from(UUID.randomUUID());
    var serviceProvider =
        ServiceProvider.of(
            userId,
            ProviderLocation.of(
                UserCityId.from(UUID.randomUUID()),
                UserDistrictId.from(UUID.randomUUID()),
                UserQuarterId.from(UUID.randomUUID())),
            PhoneNumber.from("+237", "678901234"),
            AboutProvider.from("Original profile"),
            List.of());
    var location =
        ProviderLocation.of(
            UserCityId.from(UUID.randomUUID()),
            UserDistrictId.from(UUID.randomUUID()),
            UserQuarterId.from(UUID.randomUUID()));
    var phoneNumber = PhoneNumber.from("+237", "699123456");
    var about = AboutProvider.from("Updated profile");
    var command =
        new UpdateServiceProviderProfileUseCase.Command(userId, location, phoneNumber, about);
    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(serviceProvider);

    objectUnderTest.execute(command);

    assertThat(serviceProvider.getLocation()).isEqualTo(location);
    assertThat(serviceProvider.getPhoneNumber()).isEqualTo(phoneNumber);
    assertThat(serviceProvider.getAbout()).isEqualTo(about);
    assertThat(serviceProvider.getUpdatedAt()).isNotNull();
    verify(serviceProviderRepository).loadByUserId(userId);
    verify(serviceProviderRepository).update(serviceProvider);
  }
}
