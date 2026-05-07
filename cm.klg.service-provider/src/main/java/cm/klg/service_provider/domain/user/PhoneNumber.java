package cm.klg.service_provider.domain.user;

public record PhoneNumber(String countryCode, String number) {
  public static PhoneNumber from(String countryCode, String number) {

    return new PhoneNumber(countryCode, number);
  }
}
