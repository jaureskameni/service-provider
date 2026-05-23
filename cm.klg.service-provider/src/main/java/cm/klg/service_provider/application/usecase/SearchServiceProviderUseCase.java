package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.utils.PageData;
import cm.klg.service_provider.utils.PaginationFetchRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class SearchServiceProviderUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public Response execute(Command command) {
    var pagination = new PaginationFetchRequest(command.limit(), command.page());

    return toResponse(
        serviceProviderRepository.searchByLocationAndStatus(
            command.serviceTypeId(),
            command.cityId(),
            command.districtId(),
            command.quarterId(),
            command.status(),
            pagination));
  }

  private static Response toResponse(PageData<? extends ServiceProviderView1> pageData) {
    return new Response(new ArrayList<>(pageData.elements()), pageData.total());
  }

  public record Command(
      ServiceTypeId serviceTypeId,
      UserCityId cityId,
      @Nullable UserDistrictId districtId,
      @Nullable UserQuarterId quarterId,
      ServiceProviderStatus status,
      Integer limit,
      Integer page) {}

  public record Response(List<ServiceProviderView1> serviceProviderView1s, long count) {}
}
