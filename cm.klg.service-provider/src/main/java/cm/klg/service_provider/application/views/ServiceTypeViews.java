package cm.klg.service_provider.application.views;

import java.util.UUID;

public interface ServiceTypeViews {
  record ServiceTypeView(UUID id, String name, String category, boolean isActive) {}
}
