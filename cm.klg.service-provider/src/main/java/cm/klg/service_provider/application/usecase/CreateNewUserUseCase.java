package cm.klg.service_provider.application.usecase;

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
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

@Slf4j
public record CreateNewUserUseCase(UserRepository userRepository) {
  public void execute(CreateNewUserCommand command) {
    EmailAddress emailAddress = EmailAddress.from(command.email());
    Firstname firstname = Firstname.from(command.firstname());
    PhoneNumber phoneNumber = PhoneNumber.from(command.countryCode(), command.phoneNumber());
    Lastname lastname = Lastname.from(command.lastname());

    UserProfile userProfile = new UserProfile(firstname, lastname, emailAddress, phoneNumber);
    User newUser =
        User.reconstitute(
            UserId.from(command.id()), userProfile, false, CreatedAt.from(command.createdAt));

    userRepository.insert(newUser);
  }

  public record CreateNewUserCommand(
      UUID id,
      String lastname,
      @Nullable String firstname,
      @Nullable String email,
      String countryCode,
      String phoneNumber,
      LocalDateTime createdAt) {}
}
