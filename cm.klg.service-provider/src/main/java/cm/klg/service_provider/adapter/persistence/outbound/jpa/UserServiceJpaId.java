package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
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
@Embeddable
public class UserServiceJpaId implements Serializable {

  @Column(name = "c_service_provider_id")
  private UUID serviceProviderId;

  @Column(name = "c_service_type_id")
  private UUID serviceTypeId;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserServiceJpaId that = (UserServiceJpaId) o;
    return Objects.equals(serviceProviderId, that.serviceProviderId)
        && Objects.equals(serviceTypeId, that.serviceTypeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceProviderId, serviceTypeId);
  }
}
