package cm.klg.service_provider.adapter.rest.inbound;

import static org.springframework.http.HttpStatus.OK;

import cm.klg.common.base.adapter.inbound.rest.WithAuthenticationSupport;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.api.FavoriteApi;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.service_provider.application.usecase.AddServiceProviderToFavoritesUseCase;
import cm.klg.service_provider.application.usecase.GetMyFavoriteServiceProvidersUseCase;
import cm.klg.service_provider.application.usecase.RemoveServiceProviderFromFavoritesUseCase;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteController implements FavoriteApi, WithAuthenticationSupport {
  private final UseCaseExecutor useCaseExecutor;
  private final RestMapper restMapper;
  private final AddServiceProviderToFavoritesUseCase addServiceProviderToFavoritesUseCase;
  private final RemoveServiceProviderFromFavoritesUseCase removeServiceProviderFromFavoritesUseCase;
  private final GetMyFavoriteServiceProvidersUseCase getMyFavoriteServiceProvidersUseCase;

  @Override
  public ResponseEntity<Void> addServiceProviderToFavorites(UUID serviceProviderId) {
    useCaseExecutor.runCommand(
        () ->
            addServiceProviderToFavoritesUseCase.execute(
                new AddServiceProviderToFavoritesUseCase.Command(
                    UserId.from(getCurrentUserId()), ServiceProviderId.from(serviceProviderId))));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> removeServiceProviderFromFavorites(UUID serviceProviderId) {
    useCaseExecutor.runCommand(
        () ->
            removeServiceProviderFromFavoritesUseCase.execute(
                new RemoveServiceProviderFromFavoritesUseCase.Command(
                    UserId.from(getCurrentUserId()), ServiceProviderId.from(serviceProviderId))));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ServiceProviderPaginateDTO> getMyFavoriteServiceProviders(
      Integer limit, Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getMyFavoriteServiceProvidersUseCase.execute(
                    restMapper.toGetMyFavoriteServiceProvidersCommand(
                        getCurrentUserId(), limit, page)));
    return ResponseEntity.status(OK).body(restMapper.toServiceProviderPaginateDTO(result));
  }

  private UUID getCurrentUserId() {
    return getCurrentUser().getId().map(UUID::fromString).orElseThrow();
  }
}
