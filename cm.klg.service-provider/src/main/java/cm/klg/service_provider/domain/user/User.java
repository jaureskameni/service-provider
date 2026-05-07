package cm.klg.service_provider.domain.user;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class User {
  private final UserId id;
  @Nullable private Firstname firstname;
  private Lastname lastname;
  @Nullable private EmailAddress email;
  private PhoneNumber phoneNumber;
  private CreatedAt createdAt;

  public User(UserId id, UserProfile userProfile, CreatedAt createdAt) {
    this.id = id;
    this.firstname = userProfile.firstname();
    this.lastname = userProfile.lastname();
    this.email = userProfile.email();
    this.phoneNumber = userProfile.phoneNumber();
    this.createdAt = createdAt;
  }

  public static User reconstitute(UserId id, UserProfile userProfile) {
    return new User(id, userProfile, CreatedAt.from(LocalDateTime.now()));
  }
}
