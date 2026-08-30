package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.favorite.CannotFavoriteOwnProfileException;
import cm.klg.service_provider.domain.favorite.FavoriteProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;

public record AddServiceProviderToFavoritesUseCase(
    FavoriteProviderRepository favoriteProviderRepository,
    ServiceProviderRepository serviceProviderRepository) {

  public void execute(Command command) {
    ServiceProvider serviceProvider = serviceProviderRepository.load(command.providerId());

    if (serviceProvider.getStatus() != ServiceProviderStatus.APPROVED) {
      throw new ServiceProviderNotFoundException();
    }
    if (serviceProvider.getUserId().equals(command.userId())) {
      throw new CannotFavoriteOwnProfileException();
    }

    favoriteProviderRepository.insertIfAbsent(
        FavoriteProvider.of(command.userId(), command.providerId()));
  }

  public record Command(UserId userId, ServiceProviderId providerId) {}
}
