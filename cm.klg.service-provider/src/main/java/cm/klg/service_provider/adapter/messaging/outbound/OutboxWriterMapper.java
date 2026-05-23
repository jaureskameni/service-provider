package cm.klg.service_provider.adapter.messaging.outbound;

import cm.klg.generated.service.provider.adapter.messaging.outbound.dto.ServiceProviderApprovedEventDTO;
import cm.klg.service_provider.domain.service_provider.event.ServiceProviderApprovedEvent;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OutboxWriterMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "serviceProviderId", source = "serviceProviderId.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "lastname", source = "user.lastname.value")
  @Mapping(target = "firstname", source = "user.firstname.value")
  @Mapping(target = "email", source = "user.email.value")
  @Mapping(target = "phoneNumber.number", source = "user.phoneNumber.number")
  @Mapping(target = "phoneNumber.countryCode", source = "user.phoneNumber.countryCode")
  @Mapping(target = "approvedAt", source = "approvedAt")
  ServiceProviderApprovedEventDTO toServiceProviderApprovedEventDTO(
      ServiceProviderApprovedEvent event);
}
