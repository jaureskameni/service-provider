package cm.klg.service_provider.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.klg.service_provider.domain.PhoneNumber;
import org.junit.jupiter.api.Test;

class DomainUserTest {

  @Test
  void emailAddressShouldNormalizeValue() {
    org.assertj.core.api.SoftAssertions.assertSoftly(
        softly -> {
          softly
              .assertThat(EmailAddress.from("test@example.com").value())
              .isEqualTo("test@example.com");
          softly.assertThat(EmailAddress.from(null)).isNull();
          softly.assertThat(EmailAddress.from("")).isNull();
          softly.assertThat(EmailAddress.from("   ")).isNull();
        });
  }

  @Test
  void firstnameShouldNormalizeValue() {
    assertThat(Firstname.from("  John  ").value()).isEqualTo("John");
    assertThat(Firstname.from(null)).isNull();
    assertThatThrownBy(() -> Firstname.from("")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void lastnameShouldNormalizeValueAndThrowIfEmpty() {
    assertThat(Lastname.from("  Doe  ").value()).isEqualTo("Doe");
    assertThatThrownBy(() -> Lastname.from(null)).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> Lastname.from("")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> Lastname.from("  ")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void phoneNumberShouldStoreValues() {
    PhoneNumber phoneNumber = PhoneNumber.from("+237", "699999999");
    assertThat(phoneNumber.countryCode()).isEqualTo("+237");
    assertThat(phoneNumber.number()).isEqualTo("699999999");
  }
}
