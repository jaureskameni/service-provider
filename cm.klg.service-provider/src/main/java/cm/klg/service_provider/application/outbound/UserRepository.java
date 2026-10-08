package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.User;
import org.jspecify.annotations.NonNull;

public interface UserRepository {
  void insertIfAbsent(User newUser);

  User load(UserId userId);

  void update(User user);

  void deleteById(UserId userId);

  boolean existsByUserId(UserId userId);

  boolean existsByPhoneNumberExceptUserId(@NonNull PhoneNumber phoneNumber, @NonNull UserId userId);
}
