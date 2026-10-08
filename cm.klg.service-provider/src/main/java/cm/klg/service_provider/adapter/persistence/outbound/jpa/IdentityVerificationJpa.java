package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "t_identity_verification")
public class IdentityVerificationJpa {
  @Id
  @Column(name = "c_id")
  private UUID id;

  @Column(name = "c_status", nullable = false)
  private String status;

  @Column(name = "c_cni_recto_id")
  private UUID cniRectoId;

  @Column(name = "c_cni_verso_id")
  private UUID cniVersoId;

  @Column(name = "c_rejection_reason")
  private String rejectionReason;

  @Column(name = "c_verified_at")
  private LocalDateTime verifiedAt;

  @Column(name = "c_verified_by")
  private UUID verifiedBy;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_service_provider_id", nullable = false, unique = true)
  private ServiceProviderJpa serviceProvider;
}
