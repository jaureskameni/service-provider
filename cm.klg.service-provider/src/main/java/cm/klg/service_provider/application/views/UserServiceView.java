package cm.klg.service_provider.application.views;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserServiceView {
  ServiceTypeViews.ServiceTypeView getServiceType();

  int getYearOfExperience();

  UUID getDocument();

  LocalDateTime getCreatedAt();
}
