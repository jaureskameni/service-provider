package cm.klg.service_provider.domain.service_provider;

import static cm.klg.service_provider.domain.service_provider.ServiceProviderStatus.PENDING;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderCreatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.domain.user.User;
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
  private ProviderLocation location;
  private PhoneNumber phoneNumber;
  private ServiceProviderStatus status;
  @Nullable private UserId approvedBy;
  @Nullable private UserId rejectedBy;
  @Nullable private RejectionReason rejectionReason;
  @Nullable private AboutProvider about;
  private CreatedAt createdAt;
  @Nullable private CreatedAt updatedAt;
  private final List<UserService> userServices = new ArrayList<>();

  public ServiceProvider(
      ServiceProviderId id,
      UserId userId,
      ProviderContact contact,
      ProviderReview review,
      ProviderAudit audit,
      @Nullable AboutProvider about,
      List<UserService> userServices) {
    this.id = id;
    this.userId = userId;
    this.location = contact.location();
    this.phoneNumber = contact.phoneNumber();
    this.status = review.status();
    this.approvedBy = review.approvedBy();
    this.rejectedBy = review.rejectedBy();
    this.rejectionReason = review.rejectionReason();
    this.about = about;
    this.updatedAt = audit.updatedAt();
    this.createdAt = audit.createdAt();
    this.userServices.addAll(userServices);
  }

  public List<UserService> getUserServices() {
    return java.util.Collections.unmodifiableList(userServices);
  }

  public static ServiceProvider of(
      UserId userId,
      ProviderLocation location,
      PhoneNumber phoneNumber,
      @Nullable AboutProvider about,
      List<UserService> userServices) {
    return new ServiceProvider(
        ServiceProviderId.generate(),
        userId,
        new ProviderContact(location, phoneNumber),
        new ProviderReview(PENDING, null, null, null),
        new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
        about,
        userServices);
  }

  public static ServiceProvider reconstitute(
      ServiceProviderId id,
      UserId userId,
      ProviderContact contact,
      ProviderReview review,
      ProviderAudit audit,
      @Nullable AboutProvider about,
      List<UserService> userServices) {
    return new ServiceProvider(id, userId, contact, review, audit, about, userServices);
  }

  public void addUserService(
      ServiceTypeId serviceTypeId, YearOfExperience yearOfExperience, UserDocument document) {
    boolean alreadyProvided =
        this.userServices.stream()
            .anyMatch(userService -> Objects.equals(userService.getServiceTypeId(), serviceTypeId));
    if (alreadyProvided) {
      throw new ServiceProviderAlreadyProvidesServiceException();
    }
    UserService userService = UserService.of(this.id, serviceTypeId, yearOfExperience, document);
    this.userServices.add(userService);
  }

  public void addAllUserService(List<UserService> userServices) {
    this.userServices.addAll(userServices);
  }

  public void approve(UserId userId) {
    if (!Objects.equals(this.status, ServiceProviderStatus.PENDING)) {
      throw new InvalidServiceProviderStatusTransitionException();
    }
    this.status = ServiceProviderStatus.APPROVED;
    this.approvedBy = userId;
    this.updatedAt = CreatedAt.from(LocalDateTime.now());
  }

  public void reject(UserId userId, RejectionReason reason) {
    if (!Objects.equals(this.status, ServiceProviderStatus.PENDING)) {
      throw new InvalidServiceProviderStatusTransitionException();
    }
    this.status = ServiceProviderStatus.REJECTED;
    this.rejectedBy = userId;
    this.rejectionReason = reason;
    this.updatedAt = CreatedAt.from(LocalDateTime.now());
  }

  public ServiceProviderApprovedEvent toApprovedEvent(User user) {
    return new ServiceProviderApprovedEvent(this.id, this.userId, user, LocalDateTime.now());
  }

  public ServiceProviderCreatedEvent toCreatedEvent() {
    return new ServiceProviderCreatedEvent(this.id, this.userId, LocalDateTime.now());
  }

  public ServiceProviderRejectedEvent toRejectedEvent() {
    return new ServiceProviderRejectedEvent(
        this.id,
        this.userId,
        Objects.requireNonNull(this.rejectedBy),
        Objects.requireNonNull(this.rejectionReason),
        LocalDateTime.now());
  }
}
