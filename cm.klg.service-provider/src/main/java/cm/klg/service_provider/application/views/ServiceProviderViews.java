package cm.klg.service_provider.application.views;

import cm.klg.service_provider.domain.PhoneNumber;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface ServiceProviderViews {
  record ServiceProviderView1(
      UUID id,
      UUID userId,
      String firstname,
      String lastname,
      UUID cityId,
      UUID districtId,
      @Nullable UUID quarterId,
      @Nullable UUID approvedBy,
      @Nullable UUID rejectedBy,
      @Nullable String rejectionReason,
      @Nullable String about,
      PhoneNumber phoneNumber,
      String status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {}

  record ServiceProviderView2(
      UUID id,
      UUID userId,
      String firstname,
      String lastname,
      UUID cityId,
      UUID districtId,
      @Nullable UUID quarterId,
      @Nullable UUID approvedBy,
      @Nullable UUID rejectedBy,
      @Nullable String rejectionReason,
      @Nullable String about,
      PhoneNumber phoneNumber,
      String status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      List<UserServiceView> services,
      List<PortfolioView> portfolios) {}
}
