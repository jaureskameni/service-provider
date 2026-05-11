package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserNotFoundException;
import org.jspecify.annotations.NonNull;

public record UserJpaRepository(UserSpringRepository userSpringRepository, JpaMapper jpaMapper)
    implements UserRepository {
  @Override
  public void insert(@NonNull User user) {
    userSpringRepository.save(jpaMapper.toUserJpa(user));
  }

  @Override
  public User load(@NonNull UserId userId) {
    return userSpringRepository
        .findById(userId.value())
        .map(jpaMapper::toUserDomain)
        .orElseThrow(UserNotFoundException::new);
  }
}
