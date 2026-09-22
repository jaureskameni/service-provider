package cm.klg.service_provider.domain.exception;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum ServiceProviderErrorCode implements ErrorCode {
  // ERROR-404
  SERVICE_PROVIDER_404_001("SERVICE-PROVIDER_404_001", "User Not Found"),
  SERVICE_PROVIDER_404_002("SERVICE-PROVIDER_404_002", "Service Provider Not Found"),
  SERVICE_PROVIDER_404_003("SERVICE-PROVIDER_404_003", "Service Type Not Found"),

  // ERROR-409,
  SERVICE_PROVIDER_409_001("SERVICE_PROVIDER-409-001", "This User Is Already Service Provider"),
  SERVICE_PROVIDER_409_002(
      "SERVICE_PROVIDER-409-002", "Service Provider Already Exists With this Phone Number"),
  SERVICE_PROVIDER_409_003("SERVICE_PROVIDER-409-003", "Service Provider Already Provides Service"),
  SERVICE_PROVIDER_409_004(
      "SERVICE_PROVIDER-409-004", "Invalid Service Provider Status Transition"),

  // ERROR-400
  SERVICE_PROVIDER_400_001("SERVICE_PROVIDER-400-001", "Invalid Service Provider Pagination Data"),
  SERVICE_PROVIDER_400_002("SERVICE_PROVIDER-400-002", "Cannot Favorite Own Profile"),
  SERVICE_PROVIDER_400_003("SERVICE_PROVIDER-400-003", "Invalid Service Provider Phone Number"),
  SERVICE_PROVIDER_400_004(
      "SERVICE_PROVIDER-400-004", "Invalid Service Provider Year of Experience");

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
