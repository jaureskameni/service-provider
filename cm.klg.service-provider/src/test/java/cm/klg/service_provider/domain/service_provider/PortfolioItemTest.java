package cm.klg.service_provider.domain.service_provider;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PortfolioItemTest {

  @Test
  void of_shouldCreatePortfolioItem_withGeneratedId() {
    // Given
    var title = PortfolioItemTitle.from("Project Title");
    var description = PortfolioItemDescription.from("Project Description");
    var mediaId = new PortfolioItemMediaId(UUID.randomUUID());

    // When
    var portfolioItem = PortfolioItem.of(title, description, mediaId);

    // Then
    assertThat(portfolioItem.getId()).isNotNull();
    assertThat(portfolioItem.getTitle()).isEqualTo(title);
    assertThat(portfolioItem.getDescription()).isEqualTo(description);
    assertThat(portfolioItem.getMediaId()).isEqualTo(mediaId);
    assertThat(portfolioItem.getCreatedAt()).isNotNull();
  }

  @Test
  void reconstitute_shouldCreatePortfolioItem_withAllProvidedFields() {
    // Given
    var id = new PortfolioItemId(UUID.randomUUID());
    var title = PortfolioItemTitle.from("Reconstituted Title");
    var description = PortfolioItemDescription.from("Reconstituted Description");
    var mediaId = new PortfolioItemMediaId(UUID.randomUUID());
    var createdAt = CreatedAt.from(LocalDateTime.now());

    // When
    var portfolioItem = PortfolioItem.reconstitute(id, title, description, mediaId, createdAt);

    // Then
    assertThat(portfolioItem.getId()).isEqualTo(id);
    assertThat(portfolioItem.getTitle()).isEqualTo(title);
    assertThat(portfolioItem.getDescription()).isEqualTo(description);
    assertThat(portfolioItem.getMediaId()).isEqualTo(mediaId);
    assertThat(portfolioItem.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void update_shouldUpdateAllFields() {
    // Given
    var portfolioItem =
        PortfolioItem.of(
            PortfolioItemTitle.from("Original Title"),
            PortfolioItemDescription.from("Original Description"),
            new PortfolioItemMediaId(UUID.randomUUID()));

    var newTitle = PortfolioItemTitle.from("Updated Title");
    var newDescription = PortfolioItemDescription.from("Updated Description");
    var newMediaId = new PortfolioItemMediaId(UUID.randomUUID());

    // When
    portfolioItem.update(newTitle, newDescription, newMediaId);

    // Then
    assertThat(portfolioItem.getTitle()).isEqualTo(newTitle);
    assertThat(portfolioItem.getDescription()).isEqualTo(newDescription);
    assertThat(portfolioItem.getMediaId()).isEqualTo(newMediaId);
  }

  @Test
  void update_shouldPreserveIdAndCreatedAt() {
    // Given
    var id = new PortfolioItemId(UUID.randomUUID());
    var createdAt = CreatedAt.from(LocalDateTime.now().minusDays(1));
    var portfolioItem =
        PortfolioItem.reconstitute(
            id,
            PortfolioItemTitle.from("Original"),
            PortfolioItemDescription.from("Original"),
            new PortfolioItemMediaId(UUID.randomUUID()),
            createdAt);

    var newTitle = PortfolioItemTitle.from("Updated");
    var newDescription = PortfolioItemDescription.from("Updated");
    var newMediaId = new PortfolioItemMediaId(UUID.randomUUID());

    // When
    portfolioItem.update(newTitle, newDescription, newMediaId);

    // Then
    assertThat(portfolioItem.getId()).isEqualTo(id);
    assertThat(portfolioItem.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void update_shouldUpdateOnlyProvidedFields() {
    // Given
    var originalMediaId = new PortfolioItemMediaId(UUID.randomUUID());
    var portfolioItem =
        PortfolioItem.of(
            PortfolioItemTitle.from("Title"),
            PortfolioItemDescription.from("Description"),
            originalMediaId);

    var newTitle = PortfolioItemTitle.from("New Title");
    var newDescription = PortfolioItemDescription.from("New Description");
    var newMediaId = new PortfolioItemMediaId(UUID.randomUUID());

    // When
    portfolioItem.update(newTitle, newDescription, newMediaId);

    // Then
    assertThat(portfolioItem.getTitle()).isEqualTo(newTitle);
    assertThat(portfolioItem.getDescription()).isEqualTo(newDescription);
    assertThat(portfolioItem.getMediaId()).isEqualTo(newMediaId);
    assertThat(portfolioItem.getMediaId()).isNotEqualTo(originalMediaId);
  }
}
