package cm.klg.service_provider.domain.service_type;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceType {
  private final ServiceTypeId id;
  private final ServiceTypeName name;
  private final ServiceCategory category;
  private final boolean isActive;
  private final CreatedAt createdAt;
  @Nullable private final CreatedAt updated;

  public ServiceType(
      ServiceTypeId id,
      ServiceTypeName name,
      ServiceCategory category,
      boolean isActive,
      CreatedAt createdAt,
      @Nullable CreatedAt updated) {
    this.id = Objects.requireNonNull(id);
    this.name = Objects.requireNonNull(name);
    this.category = Objects.requireNonNull(category);
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
