package cm.klg.service_provider.domain.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderPaginationDataException;
import org.junit.jupiter.api.Test;

class PaginationFetchRequestTest {

  @Test
  void shouldCreateValidPaginationFetchRequest() {
    // When
    PaginationFetchRequest request = new PaginationFetchRequest(20, 0);

    // Then
    assertThat(request.limit()).isEqualTo(20);
    assertThat(request.pageIndex()).isZero();
  }

  @Test
  void shouldThrowExceptionWhenLimitIsTooSmall() {
    assertThatThrownBy(() -> new PaginationFetchRequest(5, 0))
        .isInstanceOf(InvalidServiceProviderPaginationDataException.class);
  }

  @Test
  void shouldThrowExceptionWhenPageIndexIsNegative() {
    assertThatThrownBy(() -> new PaginationFetchRequest(20, -1))
        .isInstanceOf(InvalidServiceProviderPaginationDataException.class);
  }
}
