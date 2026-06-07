package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceProviderServiceTypeJpaRepository implements ServiceTypeRepository {
  private final ServiceTypeSpringRepository serviceTypeSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public List<ServiceTypeView> loadAllAsView1() {
    return serviceTypeSpringRepository.findAll().stream()
        .map(jpaMapper::toServiceTypeView)
        .toList();
  }
}
