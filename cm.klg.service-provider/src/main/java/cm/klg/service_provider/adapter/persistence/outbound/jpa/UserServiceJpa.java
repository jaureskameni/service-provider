package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = "t_user_service")
public class UserServiceJpa {

  @EmbeddedId private UserServiceJpaId id;

  @MapsId("serviceProviderId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_service_provider_id")
  private ServiceProviderJpa serviceProvider;

  @Column(name = "c_year_of_experience")
  private int yearOfExperience;

  @Column(name = "c_user_document")
  private UUID userDocument;

  @Column(name = "c_created_at")
  private LocalDateTime createdAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserServiceJpa that = (UserServiceJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
