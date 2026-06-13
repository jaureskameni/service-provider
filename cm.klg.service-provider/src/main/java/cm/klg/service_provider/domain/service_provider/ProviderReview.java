package cm.klg.service_provider.domain.service_provider;

import cm.klg.service_provider.domain.UserId;
import org.jspecify.annotations.Nullable;

public record ProviderReview(
    ServiceProviderStatus status,
    @Nullable UserId approvedBy,
    @Nullable UserId rejectedBy,
    @Nullable RejectionReason rejectionReason) {}
