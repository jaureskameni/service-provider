package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class GetAllServiceProviderUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public Response execute(Command command) {
    PaginationFetchRequest pagination =
        new PaginationFetchRequest(command.limit(), command.pageIndex());

    PageData<ServiceProviderView> pageData =
        command.status() != null
            ? serviceProviderRepository.loadAllByStatusAsView(
                Objects.requireNonNull(command.status()), pagination)
            : serviceProviderRepository.loadAllAsView(pagination);

    return new Response(pageData.total(), pageData.elements());
  }

  public record Command(@Nullable ServiceProviderStatus status, int limit, int pageIndex) {}

  public record Response(long count, List<ServiceProviderView> serviceProviderViews) {}
}
