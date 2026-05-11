package cm.klg.service_provider.adapter.messaging.inbound;

import cm.klg.generated.service.provider.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.service_provider.application.usecase.CreateNewUserUseCase;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

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
}
