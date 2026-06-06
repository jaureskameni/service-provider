package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class GetAllServiceProviderUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public Response execute(Command command) {
    var pagination = new PaginationFetchRequest(command.limit(), command.page());

    if (command.status() == null) {
      return toResponse(serviceProviderRepository.loadAllAsView1(pagination));
    }
    return toResponse(
        serviceProviderRepository.loadAllByStatusAsView1(
            Objects.requireNonNull(command.status()), pagination));
  }

  private static Response toResponse(PageData<? extends ServiceProviderView1> pageData) {
    return new Response(new ArrayList<>(pageData.elements()), pageData.total());
  }

  public record Command(@Nullable ServiceProviderStatus status, Integer limit, Integer page) {}

  public record Response(List<ServiceProviderView1> serviceProviderView1s, long count) {}
}
