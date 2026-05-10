package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@SuppressWarnings("JpaDataSourceORMInspection")
@FieldNameConstants
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "t_service_type")
public class ServiceTypeJpa {

  @Id
  @Column(name = "c_id")
  private UUID id;

  @Column(name = "c_name")
  private String name;

  @Column(name = "c_category")
  private String category;

  @Column(name = "c_is_active")
  private boolean isActive;

  @Column(name = "c_created_at")
  private LocalDateTime createdAt;

  @Column(name = "c_updated_at")
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ServiceTypeJpa that = (ServiceTypeJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
