package cm.klg.service_provider.config;

import cm.klg.service_provider.utils.Constants;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final ClientCredentialsProperties clientCredentialsProperties;
  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    Collection<GrantedAuthority> authorities =
        Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream())
            .collect(Collectors.toSet());

    String principalName = jwt.getClaimAsString("sub");
    return new JwtAuthenticationToken(jwt, authorities, principalName);
  }

  private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
    if (!(jwt.getClaimAsMap(Constants.Keycloak.RESOURCE_ACCESS)
        instanceof Map<?, ?> resourceAccess)) return List.of();

    if (!(resourceAccess.get(clientCredentialsProperties.clientId())
        instanceof Map<?, ?> clientAccess)) return List.of();

    if (!(clientAccess.get(Constants.Keycloak.ROLES) instanceof Collection<?> roles))
      return List.of();

    return roles.stream()
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .map(role -> new SimpleGrantedAuthority(Constants.Keycloak.ROLE_PREFIX + role))
        .collect(Collectors.toSet());
  }
}
