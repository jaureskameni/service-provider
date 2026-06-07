package cm.klg.service_provider.application.views;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserServiceView(
    ServiceTypeViews.ServiceTypeView serviceType,
    int yearOfExperience,
    UUID document,
    LocalDateTime createdAt) {}
