package cm.klg.service_provider.application.views;

import cm.klg.service_provider.domain.PhoneNumber;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface ServiceProviderViews {
  interface ServiceProviderView1 {
    UUID getId();

    UUID getUserId();

    UUID getCityId();

    UUID getDistrictId();

    UUID getQuarterId();

    @Nullable UUID getApproveBy();

    @Nullable UUID getRejectBy();

    PhoneNumber getPhoneNumber();

    String getStatus();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    List<UserServiceView> getUserService();
  }
}
