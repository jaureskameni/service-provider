package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_provider.domain.user.EmailAddress;
import cm.klg.service_provider.domain.user.Firstname;
import cm.klg.service_provider.domain.user.Lastname;
import cm.klg.service_provider.domain.user.PhoneNumber;
import cm.klg.service_provider.domain.user.User;
import cm.klg.service_provider.domain.user.UserId;
import cm.klg.service_provider.domain.user.UserProfile;
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
  void shouldInsertUser() {
    // Given
    User user =
        User.reconstitute(
            UserId.from(UUID.randomUUID()),
            new UserProfile(
                Firstname.from("John"),
                Lastname.from("Doe"),
                EmailAddress.from("john.doe@example.com"),
                PhoneNumber.from("237", "699999999")));
    UserJpa userJpa = new UserJpa();
    when(jpaMapper.toUserJpa(user)).thenReturn(userJpa);

    // When
    userJpaRepository.insert(user);

    // Then
    verify(userSpringRepository).save(userJpa);
  }
}
