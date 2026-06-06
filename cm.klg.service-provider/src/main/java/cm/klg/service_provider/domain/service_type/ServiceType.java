package cm.klg.service_provider.domain.service_type;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceType {
  private final ServiceTypeId serviceTypeId;
  private final ServiceTypeName serviceTypeName;
  private final ServiceCategory serviceCategory;
  private final boolean isActive;
  private final CreatedAt createdAt;
  @Nullable private final CreatedAt updated;

  public ServiceType(
      ServiceTypeId serviceTypeId,
      ServiceTypeName serviceTypeName,
      ServiceCategory serviceCategory,
      boolean isActive,
      CreatedAt createdAt,
      @Nullable CreatedAt updated) {
    this.serviceTypeId = Objects.requireNonNull(serviceTypeId);
    this.serviceTypeName = Objects.requireNonNull(serviceTypeName);
    this.serviceCategory = Objects.requireNonNull(serviceCategory);
    this.isActive = isActive;
    this.createdAt = Objects.requireNonNull(createdAt);
    this.updated = updated;
  }

  public static ServiceType of(ServiceTypeName name, ServiceCategory category, boolean active) {
    return new ServiceType(
        ServiceTypeId.generate(),
        name,
        category,
        active,
        CreatedAt.from(LocalDateTime.now()),
        null);
  }
}
