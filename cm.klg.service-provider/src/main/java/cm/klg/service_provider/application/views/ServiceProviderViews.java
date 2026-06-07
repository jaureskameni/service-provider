package cm.klg.service_provider.application.views;

import cm.klg.service_provider.domain.PhoneNumber;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface ServiceProviderViews {
  interface ServiceProviderView {
    UUID getId();

    UUID getUserId();

    String getFirstname();

    String getLastname();

    UUID getCityId();

    UUID getDistrictId();

    @Nullable UUID getQuarterId();

    @Nullable UUID getApprovedBy();

    @Nullable UUID getRejectedBy();

    PhoneNumber getPhoneNumber();

    String getStatus();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    List<UserServiceView> getServices();
  }
}
