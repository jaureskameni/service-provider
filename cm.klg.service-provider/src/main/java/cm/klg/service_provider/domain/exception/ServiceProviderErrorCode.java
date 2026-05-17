package cm.klg.service_provider.domain.exception;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum ServiceProviderErrorCode implements ErrorCode {
  // ERROR-404
  SERVICE_PROVIDER_404_001("SERVICE-PROVIDER_404_001", "User Not Found"),
  SERVICE_PROVIDER_404_002("SERVICE-PROVIDER_404_002", "Service Provider Not Found"),

  // ERROR-409,
  SERVICE_PROVIDER_409_001("SERVICE_PROVIDER-409-001", "This User Is Already Service Provider"),
  SERVICE_PROVIDER_409_002(
      "SERVICE_PROVIDER-409-002", "Service Provider Already Exists With this Phone Number");

  private final String value;
  @Getter private final String description;

  ServiceProviderErrorCode(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String value() {
    return value;
  }
}
