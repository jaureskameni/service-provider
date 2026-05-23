package cm.klg.service_provider.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import cm.klg.service_provider.domain.PhoneNumber;
import org.junit.jupiter.api.Test;

class DomainUserTest {

  @Test
  void emailAddressShouldNormalizeValue() {
    assertSoftly(
        softly -> {
          softly
              .assertThat(EmailAddress.from("test@example.com").value())
              .as("Regular email should stay as is")
              .isEqualTo("test@example.com");
          softly.assertThat(EmailAddress.from(null)).as("Null email should return null").isNull();
          softly.assertThat(EmailAddress.from("")).as("Empty email should return null").isNull();
          softly.assertThat(EmailAddress.from("   ")).as("Blank email should return null").isNull();
        });
  }

  @Test
  void firstnameShouldNormalizeValue() {
    assertSoftly(
        softly -> {
          softly.assertThat(Firstname.from("  John  ").value()).isEqualTo("John");
          softly.assertThat(Firstname.from(null)).isNull();
        });

    assertThatThrownBy(() -> Firstname.from(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("cannot be blank");
  }

  @Test
  void lastnameShouldNormalizeValueAndThrowIfEmpty() {
    assertThat(Lastname.from("  Doe  ").value()).isEqualTo("Doe");

    assertSoftly(
        softly -> {
          softly
              .assertThatThrownBy(() -> Lastname.from(null))
              .isInstanceOf(NullPointerException.class);
          softly
              .assertThatThrownBy(() -> Lastname.from(""))
              .isInstanceOf(IllegalArgumentException.class);
          softly
              .assertThatThrownBy(() -> Lastname.from("  "))
              .isInstanceOf(IllegalArgumentException.class);
        });
  }

  @Test
  void phoneNumberShouldStoreValues() {
    PhoneNumber phoneNumber = PhoneNumber.from("+237", "699999999");

    assertSoftly(
        softly -> {
          softly.assertThat(phoneNumber.countryCode()).isEqualTo("+237");
          softly.assertThat(phoneNumber.number()).isEqualTo("699999999");
        });
  }

  @Test
  void phoneNumberShouldNormalizeCountryCodePrefix() {
    PhoneNumber phoneNumber = PhoneNumber.from("237", "699999999");

    assertSoftly(
        softly -> {
          softly.assertThat(phoneNumber.countryCode()).isEqualTo("+237");
          softly.assertThat(phoneNumber.number()).isEqualTo("699999999");
        });
  }
}
