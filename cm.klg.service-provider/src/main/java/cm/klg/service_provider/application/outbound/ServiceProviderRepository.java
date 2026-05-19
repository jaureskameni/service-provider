package cm.klg.service_provider.application.outbound;

import cm.klg.service_provider.application.views.ServiceProviderViews.ServiceProviderView1;
import cm.klg.service_provider.domain.PhoneNumber;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.ServiceProvider;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_provider.utils.PageData;
import cm.klg.service_provider.utils.PaginationFetchRequest;

public interface ServiceProviderRepository {
  void insert(ServiceProvider serviceProvider);

  boolean existsByUserId(UserId userId);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);

  ServiceProvider load(ServiceProviderId serviceProviderId);

  void update(ServiceProvider serviceProvider);

  PageData<ServiceProviderView1> loadAllAsView1(PaginationFetchRequest pagination);

  PageData<ServiceProviderView1> loadAllByStatusAsView1(
      ServiceProviderStatus serviceProviderStatus, PaginationFetchRequest pagination);
}
