package cm.klg.service_provider.domain;

public record PhoneNumber(String countryCode, String number) {
  public static PhoneNumber from(String countryCode, String number) {
    String normalizedCountryCode = countryCode.trim();
    String normalizedNumber = number.trim();

    if (normalizedCountryCode.isEmpty() || normalizedNumber.isEmpty()) {
      throw new IllegalArgumentException("Phone number cannot contain blank values");
    }
    if (!normalizedCountryCode.matches("^\\+[1-9]\\d{0,3}$")) {
      throw new IllegalArgumentException("Invalid country code format");
    }
    if (!normalizedNumber.matches("^\\d{6,15}$")) {
      throw new IllegalArgumentException("Invalid phone number format");
    }

    return new PhoneNumber(normalizedCountryCode, normalizedNumber);
  }

  public String value() {
    return countryCode + number;
  }
}
