package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import cm.klg.service_provider.utils.PageData;
import cm.klg.service_provider.utils.PaginationFetchRequest;
import org.jspecify.annotations.Nullable;

public interface ServiceProviderRepository {
  void insert(ServiceProvider serviceProvider);

  boolean existsByUserId(UserId userId);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);

  ServiceProvider load(ServiceProviderId serviceProviderId);

  ServiceProvider loadByUserId(UserId userId);

  void update(ServiceProvider serviceProvider);

  PageData<ServiceProviderView1> loadAllAsView1(PaginationFetchRequest pagination);

  PageData<ServiceProviderView1> loadAllByStatusAsView1(
      ServiceProviderStatus serviceProviderStatus, PaginationFetchRequest pagination);

  PageData<ServiceProviderView1> searchByLocationAndStatus(
      ServiceTypeId serviceTypeId,
      UserCityId cityId,
      @Nullable UserDistrictId districtId,
      @Nullable UserQuarterId quarterId,
      ServiceProviderStatus status,
      PaginationFetchRequest pagination);

  ServiceProviderView1 loadAsView1(ServiceProviderId serviceProviderId);
}
