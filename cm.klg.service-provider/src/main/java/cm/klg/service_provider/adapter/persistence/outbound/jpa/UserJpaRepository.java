package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpaConverter;
import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
@Slf4j
public class UserJpaRepository implements UserRepository {

  private static final PhoneNumberJpaConverter PHONE_NUMBER_CONVERTER =
      new PhoneNumberJpaConverter();

  private final UserSpringRepository userSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insertIfAbsent(@NonNull User user) {
    UserJpa userJpa = jpaMapper.toUserJpa(user);
    int insertedRows =
        userSpringRepository.insertIfAbsent(
            userJpa.getId(),
            userJpa.getFirstname(),
            userJpa.getLastname(),
            userJpa.getEmailAddress(),
            PHONE_NUMBER_CONVERTER.convertToDatabaseColumn(userJpa.getPhoneNumber()),
            userJpa.getCreatedAt());
    if (insertedRows == 0) {
      log.debug("User with id {} already exists, skipping insertion.", user.getId().value());
    }
  }

  @Override
  public User load(@NonNull UserId userId) {
    return userSpringRepository
        .findById(userId.value())
        .map(jpaMapper::toUserDomain)
        .orElseThrow(UserNotFoundException::new);
  }

  @Override
  public void update(@NonNull User user) {
    userSpringRepository
        .findById(user.getId().value())
        .ifPresentOrElse(
            userJpa -> {
              jpaMapper.toUserJpa(user, userJpa);
              userSpringRepository.save(userJpa);
            },
            () -> {
              throw new UserNotFoundException();
            });
  }

  @Override
  public boolean existsByUserId(@NonNull UserId userId) {
    return userSpringRepository.existsById(userId.value());
  }

  @Override
  public void deleteById(@NonNull UserId userId) {
    userSpringRepository.deleteById(userId.value());
  }
}
