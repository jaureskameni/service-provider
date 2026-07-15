package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.List;

public interface ServiceTypeRepository {
  boolean existsById(ServiceTypeId serviceTypeId);

  List<ServiceTypeView> loadAllAsView();
}
