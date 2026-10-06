package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public record ProfileImageReviewView(
    UUID serviceProviderId,
    UUID userId,
    UUID userRecordId,
    @Nullable String firstname,
    String lastname,
    @Nullable String emailAddress,
    LocalDateTime userCreatedAt,
    String phoneCountryCode,
    String phoneNumber,
    UUID cityId,
    UUID districtId,
    @Nullable UUID quarterId,
    String providerStatus,
    UUID currentProfileImageId,
    UUID pendingProfileImageId,
    String profileImageReviewStatus,
    @Nullable LocalDateTime submittedAt) {}
