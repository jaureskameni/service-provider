package cm.klg.service_provider.domain.service_provider;

public record PortfolioItemDescription(String value) {
  public static PortfolioItemDescription from(String value) {
    return new PortfolioItemDescription(value);
  }
}
