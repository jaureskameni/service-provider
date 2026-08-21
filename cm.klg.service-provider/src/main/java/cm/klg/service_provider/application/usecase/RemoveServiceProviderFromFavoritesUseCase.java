package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;

public record RemoveServiceProviderFromFavoritesUseCase(
    FavoriteProviderRepository favoriteProviderRepository) {

  public void execute(Command command) {
    favoriteProviderRepository.deleteByUserIdAndProviderId(command.userId(), command.providerId());
  }

  public record Command(UserId userId, ServiceProviderId providerId) {}
}
