package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.user.User;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateNewUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private CreateNewUserUseCase createNewUserUseCase;

  @Test
  void shouldCreateNewUser() {
    // Given
    UUID userId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();
    CreateNewUserUseCase.CreateNewUserCommand command =
        new CreateNewUserUseCase.CreateNewUserCommand(
            userId, "Doe", "John", "john.doe@example.com", "+237", "699999999", now);

    // When
    createNewUserUseCase.execute(command);

    // Then
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).insertIfAbsent(userCaptor.capture());

    assertThat(userCaptor.getValue())
        .satisfies(
            u -> {
              assertThat(u.getId().value()).isEqualTo(userId);
              assertThat(u.getLastname().value()).isEqualTo("Doe");
              assertThat(u.getFirstname().value()).isEqualTo("John");
              assertThat(u.getEmail().value()).isEqualTo("john.doe@example.com");
              assertThat(u.getPhoneNumber().countryCode()).isEqualTo("+237");
              assertThat(u.getPhoneNumber().number()).isEqualTo("699999999");
            });
  }
}
