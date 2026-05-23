package cm.klg.service_provider.application.views;

import java.util.UUID;

public interface ServiceTypeViews {
  interface ServiceTypeView1 {
    UUID getId();

    String getName();

    String getCategory();

    boolean getIsActive();
  }
}
