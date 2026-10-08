package cm.klg.service_provider.adapter.rest.inbound;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import cm.klg.common.base.adapter.inbound.rest.WithAuthenticationSupport;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.api.ServiceProviderApi;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreatePortfolioItemRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreateProviderServiceDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.MyServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PortfolioItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ProfileImageReviewPageDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.RejectedServiceProviderIdentityDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ResubmitServiceProviderApplicationDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderProfileImageReviewDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPublicProfileDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderVerificationDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UpdateApprovedServiceProviderProfileDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UserServiceDTO;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.AddPortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.ApproveProfileImageUseCase;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.DeletePortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.DeleteProviderServiceUseCase;
import cm.klg.service_provider.application.usecase.GetAllMyPortfolioUseCase;
import cm.klg.service_provider.application.usecase.GetAllMyServicesUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetMyServiceProviderIdentityUseCase;
import cm.klg.service_provider.application.usecase.GetMyServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetProfileImageReviewUseCase;
import cm.klg.service_provider.application.usecase.GetProfileImageReviewsUseCase;
import cm.klg.service_provider.application.usecase.GetProviderPortfolioUseCase;
import cm.klg.service_provider.application.usecase.GetProviderServicesUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderVerificationUseCase;
import cm.klg.service_provider.application.usecase.RejectProfileImageUseCase;
import cm.klg.service_provider.application.usecase.RejectServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.ResubmitRejectedServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdateApprovedServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdatePortfolioItemUseCase;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import cm.klg.service_provider.domain.service_type.ServiceTypeId;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServiceProviderController implements ServiceProviderApi, WithAuthenticationSupport {
  private final UseCaseExecutor useCaseExecutor;
  private final RestMapper restMapper;
  private final BecomeServiceProviderUseCase becomeServiceProviderUseCase;
  private final GetAllServiceProviderUseCase getAllServiceProviderUseCase;
  private final GetServiceProviderByIdUseCase getServiceProviderByIdUseCase;
  private final ApproveServiceProviderRequestUseCase approveServiceProviderRequestUseCase;
  private final RejectServiceProviderRequestUseCase rejectServiceProviderRequestUseCase;
  private final AddNewServiceUseCase addNewServiceUseCase;
  private final AddPortfolioItemUseCase addPortfolioItemUseCase;
  private final GetAllMyPortfolioUseCase getAllMyPortfolioUseCase;
  private final GetProviderPortfolioUseCase getProviderPortfolioUseCase;
  private final SearchServiceProviderUseCase searchServiceProviderUseCase;
  private final UpdatePortfolioItemUseCase updatePortfolioItemUseCase;
  private final DeletePortfolioItemUseCase deletePortfolioItemUseCase;
  private final GetAllMyServicesUseCase getAllMyServicesUseCase;
  private final GetProviderServicesUseCase getProviderServicesUseCase;
  private final GetMyServiceProviderUseCase getMyServiceProviderUseCase;
  private final GetMyServiceProviderIdentityUseCase getMyServiceProviderIdentityUseCase;
  private final GetServiceProviderVerificationUseCase getServiceProviderVerificationUseCase;
  private final GetProfileImageReviewsUseCase getProfileImageReviewsUseCase;
  private final GetProfileImageReviewUseCase getProfileImageReviewUseCase;
  private final ResubmitRejectedServiceProviderUseCase resubmitRejectedServiceProviderUseCase;
  private final UpdateApprovedServiceProviderUseCase updateApprovedServiceProviderUseCase;
  private final ApproveProfileImageUseCase approveProfileImageUseCase;
  private final RejectProfileImageUseCase rejectProfileImageUseCase;
  private final DeleteProviderServiceUseCase deleteProviderServiceUseCase;

  @Override
  public ResponseEntity<Void> addNewService(CreateProviderServiceDTO serviceTypeDTO) {
    useCaseExecutor.runCommand(
        () ->
            addNewServiceUseCase.execute(
                restMapper.toAddNewServiceCommand(serviceTypeDTO, getCurrentUserId())));
    return ResponseEntity.status(CREATED).build();
  }

  @Override
  public ResponseEntity<Void> addPortfolioItem(
      CreatePortfolioItemRequestDTO createPortfolioItemRequestDTO) {
    useCaseExecutor.runCommand(
        () ->
            addPortfolioItemUseCase.execute(
                restMapper.toAddPortfolioItemCommand(
                    createPortfolioItemRequestDTO, getCurrentUserId())));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> updatePortfolioItem(
      UUID portfolioId, CreatePortfolioItemRequestDTO createPortfolioItemRequestDTO) {
    useCaseExecutor.runCommand(
        () ->
            updatePortfolioItemUseCase.execute(
                restMapper.toUpdatePortfolioItemCommand(
                    portfolioId, createPortfolioItemRequestDTO, getCurrentUserId())));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deletePortfolioItem(UUID portfolioId) {
    useCaseExecutor.runCommand(
        () ->
            deletePortfolioItemUseCase.execute(
                UserId.from(getCurrentUserId()), PortfolioItemId.from(portfolioId)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> approveServiceProvider(UUID serviceProviderId) {
    useCaseExecutor.runCommand(
        () ->
            approveServiceProviderRequestUseCase.execute(
                new UserId(getCurrentUserId()), new ServiceProviderId(serviceProviderId)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> rejectServiceProvider(
      UUID serviceProviderId, String rejectionReason) {
    useCaseExecutor.runCommand(
        () ->
            rejectServiceProviderRequestUseCase.execute(
                new UserId(getCurrentUserId()),
                new ServiceProviderId(serviceProviderId),
                restMapper.toRejectionReason(rejectionReason)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ServiceProviderPaginateDTO> searchServiceProviders(
      UUID serviceTypeId,
      UUID cityId,
      Integer limit,
      UUID districtId,
      UUID quarterId,
      Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                searchServiceProviderUseCase.execute(
                    restMapper.toSearchServiceProviderCommand(
                        serviceTypeId, cityId, districtId, quarterId, page, limit)));
    return ResponseEntity.status(OK).body(restMapper.toServiceProviderPaginateDTO(result));
  }

  @Override
  public ResponseEntity<CreationResponseDTO> becomeServiceProvider(
      ServiceProviderRegisterDTO serviceProviderRegisterDTO) {
    ServiceProviderId result =
        useCaseExecutor.executeCommand(
            () ->
                becomeServiceProviderUseCase.execute(
                    restMapper.toBecomeServiceProviderCommand(
                        serviceProviderRegisterDTO, getCurrentUserId())));
    return ResponseEntity.status(CREATED).body(new CreationResponseDTO().newId(result.value()));
  }

  @Override
  public ResponseEntity<List<PortfolioItemDTO>> getAllMyPortfolioItem() {
    var result =
        useCaseExecutor.executeQuery(
            () -> getAllMyPortfolioUseCase.execute(UserId.from(getCurrentUserId())));
    return ResponseEntity.status(OK).body(restMapper.toPortfolioItemDTOs(result));
  }

  @Override
  public ResponseEntity<List<PortfolioItemDTO>> getProviderPortfolio(UUID serviceProviderId) {
    var result =
        useCaseExecutor.executeQuery(
            () -> getProviderPortfolioUseCase.execute(ServiceProviderId.from(serviceProviderId)));
    return ResponseEntity.status(OK).body(restMapper.toPortfolioItemDTOs(result));
  }

  @Override
  public ResponseEntity<List<UserServiceDTO>> getMyServices() {
    var result =
        useCaseExecutor.executeQuery(
            () -> getAllMyServicesUseCase.execute(UserId.from(getCurrentUserId())));
    return ResponseEntity.status(OK).body(restMapper.toUserServiceDTOs(result));
  }

  @Override
  public ResponseEntity<List<UserServiceDTO>> getProviderServices(UUID serviceProviderId) {
    var result =
        useCaseExecutor.executeQuery(
            () -> getProviderServicesUseCase.execute(ServiceProviderId.from(serviceProviderId)));
    return ResponseEntity.status(OK).body(restMapper.toUserServiceDTOs(result));
  }

  @Override
  public ResponseEntity<ServiceProviderPublicProfileDTO> getServiceProviderById(
      UUID serviceProviderId) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getServiceProviderByIdUseCase.execute(
                    restMapper.toGetServiceProviderByIdCommand(serviceProviderId)));
    return ResponseEntity.status(OK).body(restMapper.toServiceProviderPublicProfileDTO(result));
  }

  @Override
  public ResponseEntity<MyServiceProviderDTO> getMyServiceProvider() {
    var providerView =
        useCaseExecutor.executeQuery(
            () -> getMyServiceProviderUseCase.execute(UserId.from(getCurrentUserId())));
    return ResponseEntity.ok(restMapper.toMyServiceProviderDTO(providerView));
  }

  @Override
  public ResponseEntity<RejectedServiceProviderIdentityDTO> myServiceProviderIdentity() {
    var identity =
        useCaseExecutor.executeQuery(
            () -> getMyServiceProviderIdentityUseCase.execute(UserId.from(getCurrentUserId())));
    return ResponseEntity.ok(restMapper.toRejectedServiceProviderIdentityDTO(identity));
  }

  @Override
  public ResponseEntity<ServiceProviderVerificationDTO> getServiceProviderVerification(
      UUID serviceProviderId) {
    var providerView =
        useCaseExecutor.executeQuery(
            () ->
                getServiceProviderVerificationUseCase.execute(
                    ServiceProviderId.from(serviceProviderId)));
    return ResponseEntity.ok(restMapper.toServiceProviderVerificationDTO(providerView));
  }

  @Override
  public ResponseEntity<Void> updateRejectedServiceProvider(
      ResubmitServiceProviderApplicationDTO dto) {
    useCaseExecutor.runCommand(
        () ->
            resubmitRejectedServiceProviderUseCase.execute(
                restMapper.toResubmitRejectedServiceProviderCommand(dto, getCurrentUserId())));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> updateApprovedServiceProvider(
      UpdateApprovedServiceProviderProfileDTO dto) {
    useCaseExecutor.runCommand(
        () ->
            updateApprovedServiceProviderUseCase.execute(
                restMapper.toUpdateApprovedServiceProviderCommand(dto, getCurrentUserId())));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ProfileImageReviewPageDTO> getProfileImageReviews(
      Integer page, Integer limit) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getProfileImageReviewsUseCase.execute(
                    page != null ? page : 0, limit != null ? limit : 20));
    return ResponseEntity.ok(
        restMapper.toProfileImageReviewPageDTO(result.elements(), result.total()));
  }

  @Override
  public ResponseEntity<ServiceProviderProfileImageReviewDTO> getProfileImageReview(
      UUID serviceProviderId) {
    var review =
        useCaseExecutor.executeQuery(
            () -> getProfileImageReviewUseCase.execute(ServiceProviderId.from(serviceProviderId)));
    return ResponseEntity.ok(restMapper.toServiceProviderProfileImageReviewDTO(review));
  }

  @Override
  public ResponseEntity<Void> approveProfileImage(UUID serviceProviderId) {
    useCaseExecutor.runCommand(
        () ->
            approveProfileImageUseCase.execute(
                UserId.from(getCurrentUserId()), ServiceProviderId.from(serviceProviderId)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> rejectProfileImage(UUID serviceProviderId, String rejectionReason) {
    useCaseExecutor.runCommand(
        () ->
            rejectProfileImageUseCase.execute(
                UserId.from(getCurrentUserId()),
                ServiceProviderId.from(serviceProviderId),
                restMapper.toRejectionReason(rejectionReason)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deleteMyService(UUID serviceTypeId) {
    useCaseExecutor.runCommand(
        () ->
            deleteProviderServiceUseCase.execute(
                UserId.from(getCurrentUserId()), new ServiceTypeId(serviceTypeId)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ServiceProviderPaginateDTO> getAllServiceProvider(
      Integer limit, ServiceProviderStatusDTO status, Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getAllServiceProviderUseCase.execute(
                    restMapper.toGetAllServiceProviderCommand(limit, status, page)));
    return ResponseEntity.status(OK).body(restMapper.toServiceProviderPaginateDTO(result));
  }

  private UUID getCurrentUserId() {
    return getCurrentUser().getId().map(UUID::fromString).orElseThrow();
  }
}
