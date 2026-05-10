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
    CreateNewUserUseCase.CreateNewUserCommand command =
        new CreateNewUserUseCase.CreateNewUserCommand(
            userId,
            "Doe",
            "John",
            "john.doe@example.com",
            "+237",
            "699999999",
            LocalDateTime.now());

    // When
    createNewUserUseCase.execute(command);

    // Then
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).insert(userCaptor.capture());

    assertThat(userCaptor.getValue())
        .extracting(
            u -> u.getId().value(),
            u -> u.getLastname().value(),
            u -> u.getFirstname().value(),
            u -> u.getEmail().value(),
            u -> u.getPhoneNumber().countryCode(),
            u -> u.getPhoneNumber().number())
        .containsExactly(userId, "Doe", "John", "john.doe@example.com", "+237", "699999999");
  }
}
