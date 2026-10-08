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
  private final ServiceProviderId id;
  private final UserId userId;
  private ProviderLocation location;
  private PhoneNumber phoneNumber;
  private ServiceProviderStatus status;
  @Nullable private UserId approvedBy;
  @Nullable private UserId rejectedBy;
  @Nullable private AboutProvider about;
  private IdentityVerification identityVerification;
  @Nullable private ProfileImageMediaId profileImageId;
  @Nullable private ProfileImageMediaId pendingProfileImageId;
  private ProfileImageReviewStatus profileImageReviewStatus;
  @Nullable private RejectionReason profileImageRejectionReason;
  private final CreatedAt createdAt;
  @Nullable private CreatedAt updatedAt;
  private final List<UserService> userServices = new ArrayList<>();
  private final List<PortfolioItem> portfolioItems = new ArrayList<>();

  public ServiceProvider(ServiceProviderId id, UserId userId, ServiceProviderState state) {
    this.id = id;
    this.userId = userId;
    this.location = state.profile().contact().location();
    this.phoneNumber = state.profile().contact().phoneNumber();
    this.about = state.profile().about();
    this.status = state.review().status();
    this.approvedBy = state.review().approvedBy();
    this.rejectedBy = state.review().rejectedBy();
    this.identityVerification = state.identityVerification();
    this.profileImageId = state.images().current();
    this.pendingProfileImageId = state.images().pending();
    this.profileImageReviewStatus = state.images().reviewStatus();
    this.profileImageRejectionReason = state.images().rejectionReason();
    this.updatedAt = state.audit().updatedAt();
    this.createdAt = state.audit().createdAt();
    addAllUserService(state.collections().userServices());
    addAllPortfolioItems(state.collections().portfolioItems());
  }

  public List<UserService> getUserServices() {
    return java.util.Collections.unmodifiableList(userServices);
  }

  public List<PortfolioItem> getPortfolioItems() {
    return java.util.Collections.unmodifiableList(portfolioItems);
  }

  public static ServiceProvider of(
      UserId userId,
      ProviderLocation location,
      PhoneNumber phoneNumber,
      @Nullable AboutProvider about,
      IdentityDocuments identityDocuments,
      ProfileImageMediaId profileImageId,
      List<UserService> userServices) {
    return new ServiceProvider(
        ServiceProviderId.generate(),
        userId,
        new ServiceProviderState(
            new ProviderProfile(new ProviderContact(location, phoneNumber), about),
            new ProviderReview(PENDING, null, null, null),
            new ProviderAudit(CreatedAt.from(LocalDateTime.now()), null),
            IdentityVerification.of(
                CniRectoMediaId.from(identityDocuments.cniRectoId().value()),
                CniVersoMediaId.from(identityDocuments.cniVersoId().value())),
            new ProviderImages(profileImageId, null, ProfileImageReviewStatus.NONE, null),
            new ServiceCollections(userServices, new ArrayList<>())));
  }

  public static ServiceProvider reconstitute(
      ServiceProviderId id, UserId userId, ServiceProviderState state) {
    return new ServiceProvider(id, userId, state);
  }

  public void addUserService(
      ServiceTypeId serviceTypeId, YearOfExperience yearOfExperience, UserDocument document) {
    addUserService(UserService.of(this.id, serviceTypeId, yearOfExperience, document));
    updatedAt();
  }

  public void addUserServiceWhenApproved(
      ServiceTypeId serviceTypeId, YearOfExperience yearOfExperience, UserDocument document) {
    ensureStatus(ServiceProviderStatus.APPROVED);
    addUserService(serviceTypeId, yearOfExperience, document);
  }

  public void addPortfolioItem(
      PortfolioItemTitle title,
      PortfolioItemDescription description,
      PortfolioItemMediaId mediaId) {
    ensureStatus(ServiceProviderStatus.APPROVED);
    PortfolioItem portfolioItem = PortfolioItem.of(title, description, mediaId);
    portfolioItems.add(portfolioItem);
    updatedAt();
  }

  public void updatePortfolioItem(
      PortfolioItemId portfolioItemId,
      PortfolioItemTitle title,
      PortfolioItemDescription description,
      PortfolioItemMediaId mediaId) {
    ensureStatus(ServiceProviderStatus.APPROVED);
    portfolioItems.stream()
        .filter(item -> item.getId().equals(portfolioItemId))
        .findFirst()
        .orElseThrow(PortfolioItemNotFoundException::new)
        .update(title, description, mediaId);
    updatedAt();
  }

  public void deletePortfolioItem(PortfolioItemId portfolioItemId) {
    ensureStatus(ServiceProviderStatus.APPROVED);
    boolean removed = portfolioItems.removeIf(item -> item.getId().equals(portfolioItemId));
    if (!removed) {
      throw new PortfolioItemNotFoundException();
    }
    updatedAt();
  }

  public void updateApprovedProfile(
      ProviderLocation location,
      PhoneNumber phoneNumber,
      @Nullable AboutProvider about,
      ProfileImageMediaId requestedProfileImageId) {
    ensureStatus(ServiceProviderStatus.APPROVED);
    this.location = location;
    this.phoneNumber = phoneNumber;
    this.about = about;
    updatedAt();
    submitProfileImageChange(requestedProfileImageId);
  }

  public void resubmit(
      ProviderLocation location,
      PhoneNumber phoneNumber,
      @Nullable AboutProvider about,
      @Nullable IdentityDocuments identityDocuments,
      ProfileImageMediaId profileImageId) {
    ensureStatus(ServiceProviderStatus.REJECTED);
    if (identityDocuments == null) {
      throw new MissingIdentityDocumentsException();
    }
    this.location = location;
    this.phoneNumber = phoneNumber;
    this.about = about;
    this.identityVerification.resubmit(
        CniRectoMediaId.from(identityDocuments.cniRectoId().value()),
        CniVersoMediaId.from(identityDocuments.cniVersoId().value()));
    this.profileImageId = profileImageId;
    this.pendingProfileImageId = null;
    this.profileImageReviewStatus = ProfileImageReviewStatus.NONE;
    this.profileImageRejectionReason = null;
    this.status = ServiceProviderStatus.PENDING;
    this.rejectedBy = null;
    this.approvedBy = null;
    updatedAt();
  }

  public void submitProfileImageChange(ProfileImageMediaId pendingProfileImageId) {
    this.pendingProfileImageId = pendingProfileImageId;
    this.profileImageReviewStatus = ProfileImageReviewStatus.PENDING_REVIEW;
    this.profileImageRejectionReason = null;
  }

  public void approveProfileImage() {
    if (this.status != ServiceProviderStatus.APPROVED
        || this.profileImageReviewStatus != ProfileImageReviewStatus.PENDING_REVIEW
        || this.pendingProfileImageId == null) {
      throw new InvalidServiceProviderStatusException();
    }
    this.profileImageId = this.pendingProfileImageId;
    this.pendingProfileImageId = null;
    this.profileImageReviewStatus = ProfileImageReviewStatus.NONE;
    this.profileImageRejectionReason = null;
    updatedAt();
  }

  public void rejectProfileImage(RejectionReason reason) {
    if (this.status != ServiceProviderStatus.APPROVED
        || this.profileImageReviewStatus != ProfileImageReviewStatus.PENDING_REVIEW
        || this.pendingProfileImageId == null) {
      throw new InvalidServiceProviderStatusException();
    }
    this.pendingProfileImageId = null;
    this.profileImageReviewStatus = ProfileImageReviewStatus.REJECTED;
    this.profileImageRejectionReason = reason;
    updatedAt();
  }

  public void addAllUserService(List<UserService> userServices) {
    userServices.forEach(this::addUserService);
  }

  public void deleteUserService(ServiceTypeId serviceTypeId) {
    ensureStatus(ServiceProviderStatus.APPROVED);
    boolean removed =
        userServices.removeIf(service -> service.getServiceTypeId().equals(serviceTypeId));
    if (!removed) {
      throw new UserServiceNotFoundException();
    }
    updatedAt();
  }

  private void addAllPortfolioItems(List<PortfolioItem> portfolioItems) {
    this.portfolioItems.addAll(portfolioItems);
  }

  private void addUserService(UserService userService) {
    boolean alreadyProvided =
        this.userServices.stream()
            .anyMatch(
                existingUserService ->
                    Objects.equals(
                        existingUserService.getServiceTypeId(), userService.getServiceTypeId()));
    if (alreadyProvided) {
      throw new ServiceProviderAlreadyProvidesServiceException();
    }
    this.userServices.add(userService);
  }

  public void approve(UserId userId) {
    if (this.status != ServiceProviderStatus.PENDING) {
      throw new InvalidServiceProviderStatusException();
    }
    this.status = ServiceProviderStatus.APPROVED;
    this.approvedBy = userId;
    this.identityVerification.approve(userId);
    updatedAt();
  }

  public void reject(UserId userId, RejectionReason reason) {
    if (this.status != ServiceProviderStatus.PENDING) {
      throw new InvalidServiceProviderStatusException();
    }
    this.status = ServiceProviderStatus.REJECTED;
    this.rejectedBy = userId;
    this.identityVerification.reject(reason);
    updatedAt();
  }

  private void ensureStatus(ServiceProviderStatus status) {
    if (this.status != status) {
      throw new InvalidServiceProviderStatusException();
    }
  }

  private void updatedAt() {
    this.updatedAt = CreatedAt.from(LocalDateTime.now());
  }
}
