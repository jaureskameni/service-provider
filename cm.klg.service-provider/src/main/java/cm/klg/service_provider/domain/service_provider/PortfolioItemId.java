package cm.klg.service_provider.domain.service_provider;

import java.util.UUID;

public record PortfolioItemId(UUID value) {
  public static PortfolioItemId generate() {
    return new PortfolioItemId(UUID.randomUUID());
  }

  public static PortfolioItemId from(UUID value) {
    return new PortfolioItemId(value);
  }
}
