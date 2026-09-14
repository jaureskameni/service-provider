package cm.klg.service_provider.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UpdateUserUseCase updateUserUseCase;

  @Test
  void shouldUpdateUser() {
    // Given
    UUID userId = UUID.randomUUID();
    User existingUser =
        User.reconstitute(
            UserId.from(userId),
            new UserProfile(
                Firstname.from("John"),
                Lastname.from("Doe"),
                EmailAddress.from("john.doe@example.com"),
                PhoneNumber.from("+237", "699999999")),
            false,
            CreatedAt.from(LocalDateTime.now()));

    when(userRepository.load(UserId.from(userId))).thenReturn(existingUser);

    UpdateUserUseCase.UpdateUserCommand command =
        new UpdateUserUseCase.UpdateUserCommand(
            userId, "Smith", "Jane", "jane.smith@example.com", "+237", "655555555");

    // When
    updateUserUseCase.execute(command);

    // Then
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).load(UserId.from(userId));
    verify(userRepository).update(userCaptor.capture());

    assertThat(userCaptor.getValue())
        .satisfies(
            u -> {
              assertThat(u.getId().value()).isEqualTo(userId);
              assertThat(u.getLastname().value()).isEqualTo("Smith");
              assertThat(u.getFirstname().value()).isEqualTo("Jane");
              assertThat(u.getEmail().value()).isEqualTo("jane.smith@example.com");
              assertThat(u.getPhoneNumber().countryCode()).isEqualTo("+237");
              assertThat(u.getPhoneNumber().number()).isEqualTo("655555555");
            });
  }
}
