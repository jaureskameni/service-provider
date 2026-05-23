package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView1;
import java.util.List;

public interface ServiceTypeRepository {
  List<ServiceTypeView1> loadAllAsView1();
}
