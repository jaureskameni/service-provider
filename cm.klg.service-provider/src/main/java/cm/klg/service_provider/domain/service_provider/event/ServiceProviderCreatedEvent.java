package cm.klg.service_provider.domain.service_provider.event;

import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.time.LocalDateTime;

public record ServiceProviderCreatedEvent(
    ServiceProviderId serviceProviderId, UserId userId, LocalDateTime createdAt) {}
