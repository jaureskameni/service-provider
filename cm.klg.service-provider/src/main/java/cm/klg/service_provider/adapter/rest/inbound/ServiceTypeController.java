package cm.klg.service_provider.adapter.rest.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.api.ServiceTypeApi;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceCatalogItemDTO;
import cm.klg.service_provider.application.usecase.GetAllServiceTypesUseCase;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServiceTypeController implements ServiceTypeApi {
  private final UseCaseExecutor useCaseExecutor;
  private final GetAllServiceTypesUseCase getAllServiceTypesUseCase;
  private final RestMapper restMapper;

  @Override
  public ResponseEntity<Map<String, List<ServiceCatalogItemDTO>>> getServiceCatalog() {
    var result = useCaseExecutor.executeQuery(getAllServiceTypesUseCase::execute);
    return ResponseEntity.ok(restMapper.toGroupedServiceCatalogDTOs(result));
  }
}
