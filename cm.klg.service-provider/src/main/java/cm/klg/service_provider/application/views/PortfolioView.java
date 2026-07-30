package cm.klg.service_provider.application.views;

import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioView(
    UUID id, String title, String description, UUID mediaId, LocalDateTime createdAt) {}
