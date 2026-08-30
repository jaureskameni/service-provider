package cm.klg.service_provider.domain.service_provider.event;

import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.UserDocument;
import cm.klg.service_provider.domain.service_provider.YearOfExperience;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.time.LocalDateTime;

public record ServiceProviderServiceAddedEvent(
    ServiceProviderId serviceProviderId,
    UserId userId,
    ServiceTypeId serviceTypeId,
    YearOfExperience yearOfExperience,
    UserDocument documentId,
    LocalDateTime addedAt) {}
