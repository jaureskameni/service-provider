package cm.klg.service_provider.domain;

import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderPaginationDataException;
import java.util.Objects;

public record PhoneNumber(String countryCode, String number) {
  public PhoneNumber {
    Objects.requireNonNull(countryCode, "countryCode must not be null");
    Objects.requireNonNull(number, "number must not be null");
    String normalizedCountryCode = countryCode.trim();
    if (!normalizedCountryCode.startsWith("+")) {
      normalizedCountryCode = "+" + normalizedCountryCode;
    }
    String normalizedNumber = number.trim();

    if (normalizedCountryCode.isEmpty() || normalizedNumber.isEmpty()) {
      throw new InvalidServiceProviderPaginationDataException();
    }
    if (!normalizedCountryCode.matches("^\\+[1-9]\\d{0,3}$")) {
      throw new InvalidServiceProviderPaginationDataException();
    }
    if (!normalizedNumber.matches("^\\d{6,15}$")) {
      throw new InvalidServiceProviderPaginationDataException();
    }

    countryCode = normalizedCountryCode;
    number = normalizedNumber;
  }

  public static PhoneNumber from(String countryCode, String number) {
    return new PhoneNumber(countryCode, number);
  }

  public String value() {
    return countryCode + number;
  }
}
