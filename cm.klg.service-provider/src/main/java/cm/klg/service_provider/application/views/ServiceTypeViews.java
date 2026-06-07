package cm.klg.service_provider.application.views;

import java.util.UUID;

public interface ServiceTypeViews {
  interface ServiceTypeView {
    UUID getId();

    String getName();

    String getCategory();

    boolean isActive();
  }
}
