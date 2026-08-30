package cm.klg.service_provider.domain.service_provider.event;

import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;

public record ServiceProviderPortfolioItemDeletedEvent(
    ServiceProviderId serviceProviderId, UserId userId, PortfolioItemId portfolioItemId) {}
