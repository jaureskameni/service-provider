package cm.klg.service_provider.domain.exception;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum ServiceProviderErrorCode implements ErrorCode {
  // ERROR-404
  SERVICE_PROVIDER_404_001("SERVICE-PROVIDER_404_001", "User Not Found"),

  // ERROR-409,
  SERVICE_PROVIDER_409_001("SERVICE_PROVIDER-409-001", "Service Provider Already Exists");

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
