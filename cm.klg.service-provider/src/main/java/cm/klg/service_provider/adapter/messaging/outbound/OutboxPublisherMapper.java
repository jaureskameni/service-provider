package cm.klg.service_provider.adapter.messaging.outbound;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderApprovedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderCreatedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderPortfolioItemAddedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderPortfolioItemDeletedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderPortfolioItemUpdatedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderProfileUpdatedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderRejectedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderServiceAddedEventDTO;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderCreatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemAddedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemDeletedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderPortfolioItemUpdatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderProfileUpdatedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderRejectedEvent;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderServiceAddedEvent;
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
  @Mapping(target = "createdAt", source = "createdAt")
  ServiceProviderCreatedEventDTO toServiceProviderCreatedEventDTO(
      ServiceProviderCreatedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "rejectedBy", source = "rejectedBy.value")
  @Mapping(target = "reason", source = "rejectionReason.value")
  @Mapping(target = "rejectedAt", source = "rejectedAt")
  ServiceProviderRejectedEventDTO toServiceProviderRejectedEventDTO(
      ServiceProviderRejectedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "serviceTypeId", source = "serviceTypeId.value")
  @Mapping(target = "yearOfExperience", source = "yearOfExperience.value")
  @Mapping(target = "documentId", source = "documentId.value")
  @Mapping(target = "addedAt", source = "addedAt")
  ServiceProviderServiceAddedEventDTO toServiceProviderServiceAddedEventDTO(
      ServiceProviderServiceAddedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "portfolioItemId", source = "portfolioItemId.value")
  ServiceProviderPortfolioItemAddedEventDTO toServiceProviderPortfolioItemAddedEventDTO(
      ServiceProviderPortfolioItemAddedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "portfolioItemId", source = "portfolioItemId.value")
  ServiceProviderPortfolioItemUpdatedEventDTO toServiceProviderPortfolioItemUpdatedEventDTO(
      ServiceProviderPortfolioItemUpdatedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "portfolioItemId", source = "portfolioItemId.value")
  ServiceProviderPortfolioItemDeletedEventDTO toServiceProviderPortfolioItemDeletedEventDTO(
      ServiceProviderPortfolioItemDeletedEvent event);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "cityId", source = "location.cityId.value")
  @Mapping(target = "districtId", source = "location.districtId.value")
  @Mapping(target = "quarterId", source = "location.quarterId.value")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "about", source = "about.value")
  @Mapping(target = "updatedAt", source = "updatedAt")
  ServiceProviderProfileUpdatedEventDTO toServiceProviderProfileUpdatedEventDTO(
      ServiceProviderProfileUpdatedEvent event);
}
