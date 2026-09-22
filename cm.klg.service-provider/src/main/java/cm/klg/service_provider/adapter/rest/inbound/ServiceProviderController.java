package cm.klg.service_provider.adapter.rest.inbound;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import cm.klg.common.base.adapter.inbound.rest.WithAuthenticationSupport;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.provider.adapter.rest.inbound.api.ServiceProviderApi;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreatePortfolioItemRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.PortfolioItemDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.RejectionReasonDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPaginateDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderPublicProfileDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderRegisterDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceProviderStatusDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.ServiceTypeDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UpdateServiceProviderProfileRequestDTO;
import cm.klg.generated.service.provider.adapter.rest.inbound.dto.UserServiceDTO;
import cm.klg.service_provider.application.usecase.AddNewServiceUseCase;
import cm.klg.service_provider.application.usecase.AddPortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.ApproveServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.BecomeServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.DeletePortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.GetAllMyPortfolioUseCase;
import cm.klg.service_provider.application.usecase.GetAllMyServicesUseCase;
import cm.klg.service_provider.application.usecase.GetAllServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.GetProviderPortfolioUseCase;
import cm.klg.service_provider.application.usecase.GetProviderServicesUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderByIdUseCase;
import cm.klg.service_provider.application.usecase.GetServiceProviderProfileUseCase;
import cm.klg.service_provider.application.usecase.RejectServiceProviderRequestUseCase;
import cm.klg.service_provider.application.usecase.SearchServiceProviderUseCase;
import cm.klg.service_provider.application.usecase.UpdatePortfolioItemUseCase;
import cm.klg.service_provider.application.usecase.UpdateServiceProviderProfileUseCase;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.PortfolioItemId;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
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
  private final GetServiceProviderProfileUseCase getServiceProviderProfileUseCase;
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
  private final UpdateServiceProviderProfileUseCase updateServiceProviderProfileUseCase;

  @Override
  public ResponseEntity<Void> addNewService(ServiceTypeDTO serviceTypeDTO) {
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
      UUID serviceProviderId, RejectionReasonDTO rejectionReasonDTO) {
    useCaseExecutor.runCommand(
        () ->
            rejectServiceProviderRequestUseCase.execute(
                new UserId(getCurrentUserId()),
                new ServiceProviderId(serviceProviderId),
                restMapper.toRejectionReason(rejectionReasonDTO)));
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
  public ResponseEntity<ServiceProviderDTO> getServiceProviderProfile() {
    var result =
        useCaseExecutor.executeQuery(
            () -> getServiceProviderProfileUseCase.execute(UserId.from(getCurrentUserId())));
    return ResponseEntity.status(OK).body(restMapper.toServiceProviderProfileDTO(result));
  }

  @Override
  public ResponseEntity<Void> updateServiceProviderProfile(
      UpdateServiceProviderProfileRequestDTO updateServiceProviderProfileRequestDTO) {
    useCaseExecutor.runCommand(
        () ->
            updateServiceProviderProfileUseCase.execute(
                restMapper.toUpdateServiceProviderProfileCommand(
                    updateServiceProviderProfileRequestDTO, getCurrentUserId())));
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
