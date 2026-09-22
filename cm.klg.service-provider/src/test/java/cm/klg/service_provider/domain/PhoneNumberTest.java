package cm.klg.service_provider.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderPaginationDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PhoneNumberTest {

  @Test
  void shouldCreateValidPhoneNumber() {
    // When
    PhoneNumber phoneNumber = new PhoneNumber("+237", "678901234");

    // Then
    assertThat(phoneNumber.countryCode()).isEqualTo("+237");
    assertThat(phoneNumber.number()).isEqualTo("678901234");
    assertThat(phoneNumber.value()).isEqualTo("+237678901234");
  }

  @Test
  void shouldNormalizeCountryCode_whenPlusIsMissing() {
    // When
    PhoneNumber phoneNumber = new PhoneNumber("237", "678901234");

    // Then
    assertThat(phoneNumber.countryCode()).isEqualTo("+237");
  }

  @ParameterizedTest
  @CsvSource({
    "'', 678901234",
    "+237, ''",
    "abc, 678901234",
    "+237, abc",
    "+23744, 678901234",
    "+237, 12345"
  })
  void shouldThrowException_whenDataIsInvalid(String countryCode, String number) {
    assertThatThrownBy(() -> new PhoneNumber(countryCode, number))
        .isInstanceOf(InvalidServiceProviderPaginationDataException.class);
  }
}
