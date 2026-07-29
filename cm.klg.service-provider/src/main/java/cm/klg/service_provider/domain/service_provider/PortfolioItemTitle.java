package cm.klg.service_provider.domain.service_provider;

public record PortfolioItemTitle(String value) {
  public static PortfolioItemTitle from(String value) {
    return new PortfolioItemTitle(value);
  }
}
