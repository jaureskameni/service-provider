package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserJpaRepositoryTest {

  @Mock private UserSpringRepository userSpringRepository;
  @Mock private JpaMapper jpaMapper;

  @InjectMocks private UserJpaRepository userJpaRepository;

  @Test
  void shouldInsertUserIfAbsent() {
    // Given
    User user =
        User.reconstitute(
            UserId.from(UUID.randomUUID()),
            new UserProfile(
                Firstname.from("John"),
                Lastname.from("Doe"),
                EmailAddress.from("john.doe@example.com"),
                PhoneNumber.from("+237", "699999999")),
            false,
            CreatedAt.from(LocalDateTime.now()));
    UserJpa userJpa = new UserJpa();
    userJpa.setId(user.getId().value());
    userJpa.setCreatedAt(user.getCreatedAt().value());
    when(jpaMapper.toUserJpa(user)).thenReturn(userJpa);
    when(userSpringRepository.insertIfAbsent(any(), any(), any(), any(), any(), any()))
        .thenReturn(1);

    // When
    userJpaRepository.insertIfAbsent(user);

    // Then
    verify(userSpringRepository)
        .insertIfAbsent(
            eq(user.getId().value()), any(), any(), any(), any(), eq(user.getCreatedAt().value()));
  }
}
