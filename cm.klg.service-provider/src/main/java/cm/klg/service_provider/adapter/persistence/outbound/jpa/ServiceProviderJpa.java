package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.AggregateRootEntity;
import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.common.base.entity.PhoneNumberJpaConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "t_service_provider")
public class ServiceProviderJpa extends AggregateRootEntity<UUID> {

  @Id
  @Column(name = "c_id")
  private UUID id;

  @Column(name = "c_user_id")
  private UUID userId;

  @Column(name = "c_city")
  private UUID city;

  @Column(name = "c_district")
  private UUID district;

  @Convert(converter = PhoneNumberJpaConverter.class)
  @Column(name = "c_phone_number")
  private PhoneNumberJpa phoneNumber;

  @Column(name = "c_status")
  private String status;

  @Column(name = "c_approved_by")
  private UUID approvedBy;

  @Column(name = "c_rejected_by")
  private UUID rejectedBy;

  @Column(name = "c_created_at")
  private LocalDateTime createdAt;

  @Column(name = "c_updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(
      mappedBy = "id.serviceProviderId",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<UserServiceJpa> userServices = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ServiceProviderJpa that = (ServiceProviderJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
