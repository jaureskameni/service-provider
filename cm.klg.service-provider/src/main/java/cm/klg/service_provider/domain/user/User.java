package cm.klg.service_provider.domain.user;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.IdentityId;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class User {
  private final UserId id;
  private final IdentityId identityId;
  @Nullable private Firstname firstname;
  private Lastname lastname;
  @Nullable private EmailAddress email;
  private PhoneNumber phoneNumber;
  private boolean isServiceProvider;
  private CreatedAt createdAt;

  User(
      UserId id,
      IdentityId identityId,
      UserProfile userProfile,
      boolean isServiceProvider,
      CreatedAt createdAt) {
    this.id = id;
    this.identityId = identityId;
    this.firstname = userProfile.firstname();
    this.lastname = userProfile.lastname();
    this.email = userProfile.email();
    this.phoneNumber = userProfile.phoneNumber();
    this.isServiceProvider = isServiceProvider;
    this.createdAt = createdAt;
  }

  public static User reconstitute(
      UserId id,
      IdentityId identityId,
      UserProfile userProfile,
      boolean isServiceProvider,
      CreatedAt createdAt) {
    return new User(id, identityId, userProfile, isServiceProvider, createdAt);
  }

  public void promoteToProvider() {
    this.isServiceProvider = true;
  }
}
