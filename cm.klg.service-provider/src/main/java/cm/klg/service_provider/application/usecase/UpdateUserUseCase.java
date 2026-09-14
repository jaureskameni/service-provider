package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserProfile;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

@Slf4j
@RequiredArgsConstructor
public class UpdateUserUseCase {
  private final UserRepository userRepository;

  public void execute(UpdateUserCommand command) {
    User existingUser = userRepository.load(UserId.from(command.userId()));

    EmailAddress emailAddress = EmailAddress.from(command.email());
    Firstname firstname = Firstname.from(command.firstname());
    PhoneNumber phoneNumber = PhoneNumber.from(command.countryCode(), command.phoneNumber());
    Lastname lastname = Lastname.from(command.lastname());

    UserProfile userProfile = new UserProfile(firstname, lastname, emailAddress, phoneNumber);
    existingUser.updateProfile(userProfile);

    userRepository.update(existingUser);
  }

  public record UpdateUserCommand(
      UUID userId,
      String lastname,
      @Nullable String firstname,
      @Nullable String email,
      String countryCode,
      String phoneNumber) {}
}
