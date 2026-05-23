package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView1;
import java.util.List;

public record ServiceProviderServiceTypeJpaRepository(
    ServiceTypeSpringRepository serviceTypeSpringRepository, JpaMapper jpaMapper)
    implements ServiceTypeRepository {

  @Override
  public List<ServiceTypeView1> loadAllAsView1() {
    return serviceTypeSpringRepository.findAll().stream()
        .map(jpaMapper::toServiceTypeView1)
        .toList();
  }
}
