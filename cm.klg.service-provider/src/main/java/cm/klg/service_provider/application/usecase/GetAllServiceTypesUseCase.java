package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView1;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllServiceTypesUseCase {
  private final ServiceTypeRepository serviceTypeRepository;

  public List<ServiceTypeView1> execute() {
    return serviceTypeRepository.loadAllAsView1();
  }
}
