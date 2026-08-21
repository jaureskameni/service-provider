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
@Table(name = "t_favorite_provider")
public class FavoriteProviderJpa {
  @Id
  @Column(name = "c_id")
  private UUID id;

  @Column(name = "c_user_id")
  private UUID userId;

  @Column(name = "c_provider_id")
  private UUID providerId;

  @Column(name = "c_created_at")
  private LocalDateTime createdAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    FavoriteProviderJpa that = (FavoriteProviderJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
