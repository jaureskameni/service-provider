package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserUpdatedEventDTO;
import cm.klg.service_provider.application.usecase.CreateNewProviderClientUseCase;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import cm.klg.service_provider.application.usecase.UpdateUserUseCase;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.SRServiceRequestAcceptedEventDTO;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessagingInboundMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "firstname", source = "firstname")
  @Mapping(target = "lastname", source = "lastname")
  @Mapping(target = "countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber", source = "phoneNumber.number")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "createdAt", source = "createdAt")
  CreateNewUserUseCase.CreateNewUserCommand toCreateUserCommand(
      UamUserCreatedEventDTO userCreatedEventDTO);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId", source = "id")
  @Mapping(target = "firstname", source = "firstname")
  @Mapping(target = "lastname", source = "lastname")
  @Mapping(target = "countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber", source = "phoneNumber.number")
  @Mapping(target = "email", source = "email")
  UpdateUserUseCase.UpdateUserCommand toUpdateUserCommand(
      UamUserUpdatedEventDTO userUpdatedEventDTO);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "providerId.value", source = "providerId")
  @Mapping(target = "userId.value", source = "userId")
  @Mapping(target = "createdAt.value", source = "updatedAt")
  CreateNewProviderClientUseCase.Command toCreateProviderClientCommand(
      SRServiceRequestAcceptedEventDTO serviceRequestAcceptedEventDTO);
}
