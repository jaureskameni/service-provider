package cm.klg.service_provider.domain.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PageDataTest {

  @Test
  void shouldCreatePageData() {
    // Given
    List<String> elements = List.of("A", "B");
    long total = 10;

    // When
    PageData<String> pageData = new PageData<>(total, elements);

    // Then
    assertThat(pageData.total()).isEqualTo(total);
    assertThat(pageData.elements()).isEqualTo(elements);
    assertThat(pageData.stream()).containsExactly("A", "B");
  }
}
