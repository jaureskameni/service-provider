package cm.klg.service_provider.domain.service_provider;

import org.jspecify.annotations.Nullable;

public record AboutProvider(@Nullable String value) {
  public static AboutProvider from(String value) {
    return new AboutProvider(value);
  }
}
