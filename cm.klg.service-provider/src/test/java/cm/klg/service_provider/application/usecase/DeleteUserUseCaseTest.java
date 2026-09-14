package cm.klg.service_provider.application.usecase;

import static org.mockito.Mockito.verify;

import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private DeleteUserUseCase deleteUserUseCase;

  @Test
  void shouldDeleteUserTest() {
    // Given
    UUID userIdValue = UUID.randomUUID();
    UserId userId = UserId.from(userIdValue);

    // When
    deleteUserUseCase.execute(userId);

    // Then
    verify(userRepository).deleteById(userId);
  }
}
