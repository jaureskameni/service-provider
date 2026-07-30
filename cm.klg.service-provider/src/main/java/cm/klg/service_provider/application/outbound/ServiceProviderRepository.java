package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.domain.service_provider.UserCityId;
import cm.klg.service_provider.domain.service_provider.UserDistrictId;
import cm.klg.service_provider.domain.service_provider.UserQuarterId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface ServiceProviderRepository {
  void insert(ServiceProvider serviceProvider);

  boolean existsByUserId(UserId userId);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);

  ServiceProvider load(ServiceProviderId serviceProviderId) throws ServiceProviderNotFoundException;

  ServiceProvider loadByUserId(UserId userId) throws ServiceProviderNotFoundException;

  void update(ServiceProvider serviceProvider);

  PageData<ServiceProviderView> loadAllAsView(PaginationFetchRequest pagination);

  PageData<ServiceProviderView> loadAllByStatusAsView(
      ServiceProviderStatus serviceProviderStatus, PaginationFetchRequest pagination);

  PageData<ServiceProviderView> searchByLocationAndStatus(
      ServiceTypeId serviceTypeId,
      UserCityId cityId,
      @Nullable UserDistrictId districtId,
      @Nullable UserQuarterId quarterId,
      ServiceProviderStatus status,
      PaginationFetchRequest pagination);

  ServiceProviderView loadAsView(ServiceProviderId serviceProviderId)
      throws ServiceProviderNotFoundException;

  ServiceProviderView loadProfile(ServiceProviderId serviceProviderId)
      throws ServiceProviderNotFoundException;

  List<PortfolioView> loadAllPortfolio(UserId userId);
}
