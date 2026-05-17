package cm.klg.service_provider.domain.service_type;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceType {
  ServiceTypeId serviceTypeId;
  ServiceTypeName serviceTypeName;
  ServiceCategory serviceCategory;
  boolean isActive;
  private CreatedAt createdAt;
  @Nullable private CreatedAt updated;

  public ServiceType(
      ServiceTypeId serviceTypeId,
      ServiceTypeName serviceTypeName,
      boolean isActive,
      CreatedAt createdAt,
      @Nullable CreatedAt updated) {
    this.serviceTypeId = serviceTypeId;
    this.serviceTypeName = serviceTypeName;
    this.isActive = isActive;
    this.createdAt = createdAt;
    this.updated = updated;
  }

  public ServiceType of(ServiceTypeName name, boolean active) {
    return new ServiceType(serviceTypeId, name, active, CreatedAt.from(LocalDateTime.now()), null);
  }
}
