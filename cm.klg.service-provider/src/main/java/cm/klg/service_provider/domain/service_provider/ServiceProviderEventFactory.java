package cm.klg.service_provider.domain.service_provider;

import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileImageApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileImageRejectedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import java.time.LocalDateTime;
import java.util.Objects;

public final class ServiceProviderEventFactory {

  private ServiceProviderEventFactory() {}

  public static ServiceProviderApprovedEvent serviceProviderApproved(ServiceProvider provider) {
    return new ServiceProviderApprovedEvent(
        provider.getId(),
        provider.getUserId(),
        Objects.requireNonNull(provider.getApprovedBy()),
        LocalDateTime.now());
  }

  public static ServiceProviderRejectedEvent serviceProviderRejected(ServiceProvider provider) {
    return new ServiceProviderRejectedEvent(
        provider.getId(),
        provider.getUserId(),
        Objects.requireNonNull(provider.getRejectedBy()),
        Objects.requireNonNull(provider.getIdentityVerification().getRejectionReason()),
        LocalDateTime.now());
  }

  public static ServiceProviderProfileImageApprovedEvent serviceProviderProfileImageApproved(
      ServiceProvider provider, UserId approvedBy) {
    return new ServiceProviderProfileImageApprovedEvent(
        provider.getId(),
        provider.getUserId(),
        approvedBy,
        Objects.requireNonNull(provider.getProfileImageId()),
        LocalDateTime.now());
  }

  public static ServiceProviderProfileImageRejectedEvent serviceProviderProfileImageRejected(
      ServiceProvider provider, UserId rejectedBy) {
    return new ServiceProviderProfileImageRejectedEvent(
        provider.getId(),
        provider.getUserId(),
        rejectedBy,
        Objects.requireNonNull(provider.getProfileImageRejectionReason()),
        LocalDateTime.now());
  }
}
