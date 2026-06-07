package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class SearchServiceProviderUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public Response execute(Command command) {
    PaginationFetchRequest pagination =
        new PaginationFetchRequest(command.pageIndex(), command.limit());

    PageData<ServiceProviderView> result =
        serviceProviderRepository.searchByLocationAndStatus(
            command.serviceTypeId(),
            command.cityId(),
            command.districtId(),
            command.quarterId(),
            command.status(),
            pagination);

    return new Response(result.total(), result.elements());
  }

  public record Command(
      ServiceTypeId serviceTypeId,
      UserCityId cityId,
      @Nullable UserDistrictId districtId,
      @Nullable UserQuarterId quarterId,
      ServiceProviderStatus status,
      int limit,
      int pageIndex) {}

  public record Response(long count, List<ServiceProviderView> serviceProviderView1s) {}
}
