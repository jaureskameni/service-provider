package cm.klg.service_provider.domain.service_provider;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserService {
  private ServiceProviderId serviceProviderId;
  private ServiceTypeId serviceTypeId;
  private YearOfExperience yearOfExperience;
  private UserDocument userDocument;
  private CreatedAt createdAt;

  public UserService(
      ServiceProviderId serviceProviderId,
      ServiceTypeId serviceTypeId,
      YearOfExperience yearOfExperience,
      UserDocument userDocument,
      CreatedAt createdAt) {
    this.serviceProviderId = serviceProviderId;
    this.serviceTypeId = serviceTypeId;
    this.yearOfExperience = yearOfExperience;
    this.userDocument = userDocument;
    this.createdAt = createdAt;
  }

  public static UserService of(
      ServiceProviderId serviceProviderId,
      ServiceTypeId serviceTypeId,
      YearOfExperience yearOfExperience,
      UserDocument userDocument) {
    return new UserService(
        serviceProviderId,
        serviceTypeId,
        yearOfExperience,
        userDocument,
        CreatedAt.from(LocalDateTime.now()));
  }

  public static UserService reconstitute(
      ServiceProviderId serviceProviderId,
      ServiceTypeId serviceTypeId,
      YearOfExperience yearOfExperience,
      UserDocument userDocument,
      CreatedAt createdAt) {
    return new UserService(
        serviceProviderId, serviceTypeId, yearOfExperience, userDocument, createdAt);
  }
}
