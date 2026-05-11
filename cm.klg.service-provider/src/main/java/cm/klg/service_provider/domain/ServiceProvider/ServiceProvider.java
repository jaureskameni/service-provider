package cm.klg.service_provider.domain.ServiceProvider;

import static cm.klg.service_provider.domain.ServiceProvider.ServiceProviderStatus.PENDING;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.ServiceType.ServiceTypeId;
import cm.klg.service_provider.domain.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceProvider {
  private ServiceProviderId id;
  private UserId userId;
  private UserCityId city;
  private UserDistrictId district;
  private PhoneNumber phoneNumber;
  private ServiceProviderStatus status;
  @Nullable private UserId approvedBy;
  @Nullable private UserId rejectedBy;
  private CreatedAt createdAt;
  @Nullable private CreatedAt updatedAt;
  private List<UserService> userServices = new ArrayList<>();

  public ServiceProvider(
      ServiceProviderId id,
      UserId userId,
      UserCityId city,
      UserDistrictId district,
      PhoneNumber phoneNumber,
      ServiceProviderStatus status,
      @Nullable UserId approvedBy,
      @Nullable UserId rejectedBy,
      CreatedAt createdAt,
      @Nullable CreatedAt updatedAt,
      List<UserService> userServices) {
    this.id = id;
    this.userId = userId;
    this.city = city;
    this.district = district;
    this.phoneNumber = phoneNumber;
    this.status = status;
    this.approvedBy = approvedBy;
    this.rejectedBy = rejectedBy;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.userServices = userServices;
  }

  public static ServiceProvider of(
      UserId userId,
      UserCityId city,
      UserDistrictId districtId,
      PhoneNumber phoneNumber,
      List<UserService> userServices) {
    return new ServiceProvider(
        ServiceProviderId.generate(),
        userId,
        city,
        districtId,
        phoneNumber,
        PENDING,
        null,
        null,
        CreatedAt.from(LocalDateTime.now()),
        null,
        userServices);
  }

  public void addUserService(
      ServiceTypeId serviceTypeId, YearOfExperience yearOfExperience, UserDocument document) {
    UserService userService = UserService.of(this.id, serviceTypeId, yearOfExperience, document);
    this.userServices.add(userService);
  }
}
