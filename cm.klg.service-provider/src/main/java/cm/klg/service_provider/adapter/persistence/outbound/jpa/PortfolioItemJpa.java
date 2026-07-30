package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "t_portfolio_item")
public class PortfolioItemJpa {

  @Id
  @Column(name = "c_id")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_service_provider_id")
  private ServiceProviderJpa serviceProvider;

  @Column(name = "c_title")
  private String title;

  @Column(name = "c_description")
  private String description;

  @Column(name = "c_media_id")
  private UUID mediaId;

  @Column(name = "c_created_at")
  private LocalDateTime createdAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    PortfolioItemJpa that = (PortfolioItemJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
