package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.provider_client.ProviderClient;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateNewProviderClientUseCaseTest {

  @Mock private ProviderClientRepository providerClientRepository;

  @InjectMocks private CreateNewProviderClientUseCase createNewProviderClientUseCase;

  @Test
  void shouldCreateNewProviderClientWhenNotExists() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    CreatedAt createdAt = new CreatedAt(LocalDateTime.now());
    CreateNewProviderClientUseCase.Command command =
        new CreateNewProviderClientUseCase.Command(providerId, userId, createdAt);

    when(providerClientRepository.existsByUserIdAndProviderId(userId, providerId))
        .thenReturn(false);

    // When
    createNewProviderClientUseCase.execute(command);

    // Then
    ArgumentCaptor<ProviderClient> captor = ArgumentCaptor.forClass(ProviderClient.class);
    verify(providerClientRepository).insert(captor.capture());

    assertThat(captor.getValue().getUserId()).isEqualTo(userId);
    assertThat(captor.getValue().getProviderId()).isEqualTo(providerId);
  }

  @Test
  void shouldNotCreateNewProviderClientWhenAlreadyExists() {
    // Given
    UserId userId = new UserId(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    CreatedAt createdAt = new CreatedAt(LocalDateTime.now());
    CreateNewProviderClientUseCase.Command command =
        new CreateNewProviderClientUseCase.Command(providerId, userId, createdAt);

    when(providerClientRepository.existsByUserIdAndProviderId(userId, providerId)).thenReturn(true);

    // When
    createNewProviderClientUseCase.execute(command);

    // Then
    verify(providerClientRepository, never()).insert(any());
  }
}
