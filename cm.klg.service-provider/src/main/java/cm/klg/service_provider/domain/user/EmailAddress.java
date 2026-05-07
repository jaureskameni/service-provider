package cm.klg.service_provider.domain.user;

import org.jspecify.annotations.Nullable;

public record EmailAddress(@Nullable String value) {
  @Nullable
  public static EmailAddress from(@Nullable String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    cm.klg.common.base.domain.EmailAddress emailAddress =
        new cm.klg.common.base.domain.EmailAddress(value.trim());
    return new EmailAddress(emailAddress.value());
  }
}
