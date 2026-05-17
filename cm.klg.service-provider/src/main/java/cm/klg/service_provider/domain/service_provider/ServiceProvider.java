package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.service_provider.ServiceProviderStatus.PENDING;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
      ProviderContact contact,
      ProviderReview review,
      ProviderAudit audit,
      List<UserService> userServices) {
    this.id = id;
    this.userId = userId;
    this.city = contact.city();
    this.district = contact.district();
    this.phoneNumber = contact.phoneNumber();
    this.status = review.status();
    this.approvedBy = review.approvedBy();
    this.rejectedBy = review.rejectedBy();
    this.updatedAt = audit.updatedAt();
    this.createdAt = audit.createdAt();
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
        new ProviderContact(city, districtId, phoneNumber),
        new ProviderReview(PENDING, null, null),
        new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
        userServices);
  }

  public static ServiceProvider reconstitute(
      ServiceProviderId id,
      UserId userId,
      ProviderContact contact,
      ProviderReview review,
      ProviderAudit audit,
      List<UserService> userServices) {
    return new ServiceProvider(id, userId, contact, review, audit, userServices);
  }

  public void addUserService(
      ServiceTypeId serviceTypeId, YearOfExperience yearOfExperience, UserDocument document) {
    UserService userService = UserService.of(this.id, serviceTypeId, yearOfExperience, document);
    this.userServices.add(userService);
  }

  public void addAllUserService(List<UserService> userServices) {
    this.userServices.addAll(userServices);
  }

  public void approve(UserId userId) {
    if (!Objects.equals(this.status, ServiceProviderStatus.PENDING)) {
      return;
    }
    this.status = ServiceProviderStatus.APPROVED;
    this.approvedBy = userId;
    this.updatedAt = CreatedAt.from(LocalDateTime.now());
  }

  public void reject(UserId userId) {
    if (!Objects.equals(this.status, ServiceProviderStatus.PENDING)) {
      return;
    }
    this.status = ServiceProviderStatus.APPROVED;
    this.rejectedBy = userId;
    this.updatedAt = CreatedAt.from(LocalDateTime.now());
  }
}
