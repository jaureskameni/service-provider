package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.application.views.PortfolioView;
import cm.klg.service_provider.application.views.ServiceProviderViews;
import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.application.views.UserServiceView;
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

  boolean existsByPhoneNumberExceptProviderId(
      PhoneNumber phoneNumber, ServiceProviderId providerId);

  ServiceProvider load(ServiceProviderId serviceProviderId) throws ServiceProviderNotFoundException;

  ServiceProvider loadByUserId(UserId userId) throws ServiceProviderNotFoundException;

  void update(ServiceProvider serviceProvider);

  PageData<ServiceProviderView1> loadAllAsView(PaginationFetchRequest pagination);

  PageData<ServiceProviderView1> loadAllByStatusAsView(
      ServiceProviderStatus serviceProviderStatus, PaginationFetchRequest pagination);

  PageData<ServiceProviderView1> searchByLocationAndStatus(
      ServiceTypeId serviceTypeId,
      UserCityId cityId,
      @Nullable UserDistrictId districtId,
      @Nullable UserQuarterId quarterId,
      ServiceProviderStatus status,
      PaginationFetchRequest pagination);

  ServiceProviderViews.ServiceProviderView2 loadApprovedAsView2(ServiceProviderId serviceProviderId)
      throws ServiceProviderNotFoundException;

  ServiceProviderView1 loadAsView1(UserId serviceProviderId)
      throws ServiceProviderNotFoundException;

  List<PortfolioView> loadAllMyPortfolio(UserId userId);

  List<PortfolioView> loadAllApprovedProviderPortfolio(ServiceProviderId providerId);

  List<UserServiceView> loadAllApprovedProviderServices(ServiceProviderId providerId);

  List<UserServiceView> loadAllMyServices(UserId userId);
}
