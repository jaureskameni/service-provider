package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.FavoriteProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import java.util.List;

public record GetMyFavoriteServiceProvidersUseCase(
    FavoriteProviderRepository favoriteProviderRepository) {

  public Response execute(Command command) {
    PaginationFetchRequest pagination =
        new PaginationFetchRequest(command.limit(), command.pageIndex());
    PageData<ServiceProviderView1> pageData =
        favoriteProviderRepository.findFavoritesByUserId(command.userId(), pagination);
    return new Response(pageData.total(), pageData.elements());
  }

  public record Command(UserId userId, int limit, int pageIndex) {}

  public record Response(long count, List<ServiceProviderView1> serviceProviderViews) {}
}
