package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class GetServiceProviderByIdUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final ProviderClientRepository providerClientRepository;
  private final FavoriteProviderRepository favoriteProviderRepository;

  public Response execute(Command command) {
    ServiceProviderId providerId = command.providerId;
    UserId userId = command.userId;

    ServiceProviderViews.ServiceProviderView2 providerView =
        serviceProviderRepository.loadAsView2(providerId);

    boolean isClient =
        userId != null && providerClientRepository.existsByUserIdAndProviderId(userId, providerId);
    boolean isFavorite =
        userId != null
            && favoriteProviderRepository.existsByUserIdAndProviderId(userId, providerId);
    return new Response(providerView, isClient, isFavorite);
  }

  public record Command(ServiceProviderId providerId, @Nullable UserId userId) {}

  public record Response(
      ServiceProviderViews.ServiceProviderView2 serviceProviderView2,
      boolean isClient,
      boolean isFavorite) {}
}
