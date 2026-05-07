package cm.klg.service_provider.domain.user;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum ServiceProviderErrorCode implements ErrorCode {
  // ERROR-404
  UAM_404_001("UAM-404-001", "User Not Found"),

  // ERROR-409,
  UAM_409_001("UAM-409-001", "User With Username Already Exists"),
  UAM_409_002("UAM-409-002", "User With Email Address Already Exists"),
  UAM_409_003("UAM-409-003", "User With Phone Number Already Exists"),
  UAM_409_004("UAM-409-004", "User Already Exists In Identity Provider"),
  // ERROR-503
  UAM_503_001("UAM-503-001", "Keycloak Failed Exception");

  private final String value;
  @Getter private final String description;

  ServiceProviderErrorCode(String value) {
    this(value, "");
  }

  ServiceProviderErrorCode(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String value() {
    return value;
  }
}
