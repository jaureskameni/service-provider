package cm.klg.service_provider.adapter.messaging.outbound;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderApprovedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderProfileImageApprovedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderProfileImageRejectedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderRejectedEventDTO;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileImageApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileImageRejectedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OutboxPublisherMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "approvedBy", source = "approvedBy.value")
  @Mapping(target = "approvedAt", source = "approvedAt")
  ServiceProviderApprovedEventDTO toServiceProviderApprovedEventDTO(
      ServiceProviderApprovedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "rejectedBy", source = "rejectedBy.value")
  @Mapping(target = "reason", source = "rejectionReason")
  @Mapping(target = "rejectedAt", source = "rejectedAt")
  ServiceProviderRejectedEventDTO toServiceProviderRejectedEventDTO(
      ServiceProviderRejectedEvent event);

  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "approvedBy", source = "approvedBy.value")
  @Mapping(target = "profileImageMediaId", source = "profileImageMediaId.value")
  ServiceProviderProfileImageApprovedEventDTO toServiceProviderProfileImageApprovedEventDTO(
      ServiceProviderProfileImageApprovedEvent event);

  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "rejectedBy", source = "rejectedBy.value")
  @Mapping(target = "reason", source = "rejectionReason")
  ServiceProviderProfileImageRejectedEventDTO toServiceProviderProfileImageRejectedEventDTO(
      ServiceProviderProfileImageRejectedEvent event);
}
