package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class GetServiceProviderProfileUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final ProviderClientRepository providerClientRepository;

  public Response execute(ServiceProviderId serviceProviderId, @Nullable UserId currentUserId) {
    ServiceProviderView profile = serviceProviderRepository.loadProfile(serviceProviderId);

    boolean isClient =
        currentUserId != null
            && providerClientRepository.existsByUserIdAndProviderId(
                currentUserId, serviceProviderId);

    return new Response(profile, isClient);
  }

  public record Response(ServiceProviderView profile, boolean isClient) {}
}
