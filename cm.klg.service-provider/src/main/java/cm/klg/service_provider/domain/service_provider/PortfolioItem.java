package cm.klg.service_provider.domain.service_provider;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PortfolioItem {
  private final PortfolioItemId id;
  private PortfolioItemTitle title;
  private PortfolioItemDescription description;
  private PortfolioItemMediaId mediaId;
  private CreatedAt createdAt;

  public static PortfolioItem of(
      PortfolioItemTitle title,
      PortfolioItemDescription description,
      PortfolioItemMediaId mediaId) {
    return new PortfolioItem(
        PortfolioItemId.generate(),
        title,
        description,
        mediaId,
        new CreatedAt(LocalDateTime.now()));
  }

  public static PortfolioItem reconstitute(
      PortfolioItemId id,
      PortfolioItemTitle title,
      PortfolioItemDescription description,
      PortfolioItemMediaId mediaId,
      CreatedAt createdAt) {
    return new PortfolioItem(id, title, description, mediaId, createdAt);
  }
}
