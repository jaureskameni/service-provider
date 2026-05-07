package cm.klg.service_provider.adapter.rest.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RestMapperTest {
  private final RestMapper mapper = new RestMapperImpl();

  @Test
  void shouldBeInstantiated() {
    assertThat(mapper).isNotNull();
  }
}
