package cm.klg.service_provider.domain.service_type;

public record ServiceTypeName(String value) {
  public static ServiceTypeName from(String value) {
    return new ServiceTypeName(value);
  }
}
