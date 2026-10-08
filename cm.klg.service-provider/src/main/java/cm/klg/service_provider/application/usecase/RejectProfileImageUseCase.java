package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.DomainEventPublisher;
import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.service_provider.RejectionReason;
import cm.klg.service_provider.domain.service_provider.ServiceProviderEventFactory;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RejectProfileImageUseCase {
  private final ServiceProviderRepository repository;
  private final DomainEventPublisher eventPublisher;

  public void execute(UserId adminId, ServiceProviderId id, RejectionReason reason) {
    var provider = repository.load(id);
    provider.rejectProfileImage(reason);
    repository.update(provider);
    eventPublisher.serviceProviderProfileImageRejectedEvent(
        ServiceProviderEventFactory.serviceProviderProfileImageRejected(provider, adminId));
  }
}
