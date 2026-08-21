package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.domain.IdentityId;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.User;

public interface UserRepository {
  void insertIfAbsent(User newUser);

  User load(IdentityId identityId);

  void update(User user);

  boolean existsByUserId(UserId userId);
}
