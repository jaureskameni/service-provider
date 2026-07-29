package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record PortfolioItemMediaId(UUID value) {
  public static PortfolioItemMediaId from(UUID value) {
    return new PortfolioItemMediaId(value);
  }
}
