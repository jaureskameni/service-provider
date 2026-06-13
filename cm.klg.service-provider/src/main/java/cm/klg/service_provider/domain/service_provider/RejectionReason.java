package cm.klg.service_provider.domain.service_provider;

import org.jspecify.annotations.Nullable;

public record RejectionReason(@Nullable String value) {
  public static RejectionReason from(String value) {
    return new RejectionReason(value);
  }
}
