package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.IdentityId;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

  private final UserSpringRepository userSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(@NonNull User user) {
    userSpringRepository.save(jpaMapper.toUserJpa(user));
  }

  @Override
  public User load(@NonNull IdentityId identityId) {
    return userSpringRepository
        .findByIdentityId(identityId.value())
        .map(jpaMapper::toUserDomain)
        .orElseThrow(UserNotFoundException::new);
  }

  @Override
  public void update(@NonNull User user) {
    userSpringRepository
        .findById(user.getId().value())
        .ifPresent(
            userJpa -> {
              jpaMapper.toUserJpa(user, userJpa);
              userSpringRepository.save(userJpa);
            });
  }

  @Override
  public boolean existsByUserId(@NonNull UserId userId) {
    return userSpringRepository.existsByIdentityId(userId.value());
  }
}
