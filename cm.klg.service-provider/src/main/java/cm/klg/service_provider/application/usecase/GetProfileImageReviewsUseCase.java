package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.views.ServiceProviderViews.ProfileImageReviewSummaryView;
import cm.klg.service_provider.domain.common.PageData;
import cm.klg.service_provider.domain.common.PaginationFetchRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetProfileImageReviewsUseCase {
  private final ServiceProviderRepository repository;

  public PageData<ProfileImageReviewSummaryView> execute(int page, int limit) {
    return repository.loadPendingProfileImageReviews(
        PaginationFetchRequest.builder().pageIndex(page).limit(limit).build());
  }
}
