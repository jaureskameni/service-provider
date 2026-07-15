package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.views.ServiceTypeViews.ServiceTypeView;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@RequiredArgsConstructor
public class ServiceProviderServiceTypeJpaRepository implements ServiceTypeRepository {
  private final ServiceTypeSpringRepository serviceTypeSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public boolean existsById(@NonNull ServiceTypeId serviceTypeId) {
    return serviceTypeSpringRepository.existsById(serviceTypeId.value());
  }

  @Override
  public List<ServiceTypeView> loadAllAsView() {
    return serviceTypeSpringRepository.findAll().stream()
        .map(jpaMapper::toServiceTypeView)
        .toList();
  }
}
