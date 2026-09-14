package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.common.base.entity.PhoneNumberJpaConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "t_user")
public class UserJpa {
  @Id
  @Column(name = "c_id")
  private UUID id;

  @Column(name = "c_firstname")
  private String firstname;

  @Column(name = "c_lastname")
  private String lastname;

  @Column(name = "c_email_address")
  private String emailAddress;

  @Convert(converter = PhoneNumberJpaConverter.class)
  @Column(name = "c_phone_number")
  private PhoneNumberJpa phoneNumber;

  @Column(name = "c_is_service_provider")
  private boolean isServiceProvider;

  @Column(name = "c_created_at")
  private LocalDateTime createdAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserJpa userJpa = (UserJpa) o;
    return Objects.equals(id, userJpa.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
