package cm.klg.service_provider.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

class KeycloakJwtConverterTest {

  private final KeycloakJwtConverter objectUnderTest =
      new KeycloakJwtConverter(new JwtGrantedAuthoritiesConverter());

  @Test
  void convert_shouldKeepScopeAuthorities_whenScopeClaimIsPresent() {
    // Given
    Jwt jwt =
        jwt(
            Map.of(
                "sub", "user-id", "scope", "service-provider:read:all service-provider:approve"));

    // When
    var result = objectUnderTest.convert(jwt);

    // Then
    assertThat(result.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .contains(Scopes.SERVICE_PROVIDER_READ_ALL, Scopes.SERVICE_PROVIDER_APPROVE)
        .doesNotContain(Scopes.SERVICE_PROVIDER_REJECT);
  }

  private Jwt jwt(Map<String, Object> claims) {
    return new Jwt(
        "token", Instant.now(), Instant.now().plusSeconds(300), Map.of("alg", "none"), claims);
  }
}
