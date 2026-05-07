package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.user.User;
import org.jspecify.annotations.NonNull;

public record UserJpaRepository(UserSpringRepository userSpringRepository, JpaMapper jpaMapper)
    implements UserRepository {
  @Override
  public void insert(@NonNull User user) {
    userSpringRepository.save(jpaMapper.toUserJpa(user));
  }
}
