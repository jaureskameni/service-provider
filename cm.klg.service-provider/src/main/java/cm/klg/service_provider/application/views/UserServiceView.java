package cm.klg.service_provider.application.views;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserServiceView {
  UUID getServiceProviderId();

  UUID getServiceTypeId();

  int getYearOfExperience();

  UUID getUserDocument();

  LocalDateTime getCreatedAt();
}
