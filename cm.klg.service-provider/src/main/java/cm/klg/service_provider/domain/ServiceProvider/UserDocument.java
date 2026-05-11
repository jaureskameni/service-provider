package cm.klg.service_provider.domain.ServiceProvider;

import java.util.UUID;

public record UserDocument(UUID value) {
  public static UserDocument from(UUID value) {
    return new UserDocument(value);
  }
}
