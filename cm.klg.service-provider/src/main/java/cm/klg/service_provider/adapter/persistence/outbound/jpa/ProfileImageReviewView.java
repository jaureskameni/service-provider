package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
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
    PhoneNumberJpa phoneNumber,
    UUID cityId,
    UUID districtId,
    @Nullable UUID quarterId,
    String providerStatus,
    UUID currentProfileImageId,
    UUID pendingProfileImageId,
    String profileImageReviewStatus,
    @Nullable LocalDateTime submittedAt) {}
