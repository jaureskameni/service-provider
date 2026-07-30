package cm.klg.service_provider.domain.service_provider;

import java.util.List;

public record ServiceCollections(
    List<UserService> userServices, List<PortfolioItem> portfolioItems) {}
