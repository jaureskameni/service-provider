package cm.klg.service_provider.domain.service_provider.event;

import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.AboutProvider;
import cm.klg.service_provider.domain.service_provider.ProviderLocation;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;

public record ServiceProviderProfileUpdatedEvent(
    ServiceProviderId serviceProviderId,
    UserId userId,
    ProviderLocation location,
    PhoneNumber phoneNumber,
    @Nullable AboutProvider about,
    LocalDateTime updatedAt) {}
