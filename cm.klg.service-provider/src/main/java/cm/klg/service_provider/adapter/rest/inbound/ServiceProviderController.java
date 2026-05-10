package cm.klg.service_provider.adapter.rest.inbound;

import static org.springframework.http.HttpStatus.CREATED;

import cm.klg.common.base.adapter.inbound.rest.WithAuthenticationSupport;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.api.ServiceProviderApi;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.domain.ServiceProvider.ServiceProviderId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServiceProviderController implements ServiceProviderApi, WithAuthenticationSupport {
  private final UseCaseExecutor useCaseExecutor;
  private final RestMapper restMapper;
  private final BecomeServiceProviderUseCase becomeServiceProviderUseCase;

  @Override
  public ResponseEntity<CreationResponseDTO> becomeServiceProvider(
      ServiceProviderRegisterDTO serviceProviderRegisterDTO) {
    ServiceProviderId result =
        useCaseExecutor.executeCommand(
            () ->
                becomeServiceProviderUseCase.execute(
                    restMapper.toBecomeServiceProviderCommand(
                        serviceProviderRegisterDTO, getCurrentUserId())));
    return ResponseEntity.status(CREATED).body(new CreationResponseDTO().newId(result.value()));
  }

  private UUID getCurrentUserId() {
    return getCurrentUser().getId().map(UUID::fromString).orElseThrow();
  }
}
