package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
public class GetAllServiceTypesUseCase {
  private final ServiceTypeRepository serviceTypeRepository;

  @Cacheable("serviceTypes")
  public List<ServiceTypeView> execute() {
    return serviceTypeRepository.loadAllAsView();
  }
}
