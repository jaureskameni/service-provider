package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import java.util.List;

public interface ServiceTypeRepository {
  List<ServiceTypeView> loadAllAsView1();
}
