package cm.klg.service_provider.domain.user;

import cm.klg.service_provider.domain.PhoneNumber;
import org.jspecify.annotations.Nullable;

public record UserProfile(
    @Nullable Firstname firstname,
    Lastname lastname,
    @Nullable EmailAddress email,
    PhoneNumber phoneNumber) {}
