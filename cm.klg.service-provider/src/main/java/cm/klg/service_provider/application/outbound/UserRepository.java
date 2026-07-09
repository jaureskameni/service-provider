package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.User;

public interface UserRepository {
  void insert(User newUser);

  User load(UserId userId);

  void update(User user);

  boolean existsByUserId(UserId userId);
}
