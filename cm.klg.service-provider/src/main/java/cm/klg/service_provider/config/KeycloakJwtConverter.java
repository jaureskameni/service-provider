package cm.klg.service_provider.config;

import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

/**
 * Ce convertisseur est responsable de deux choses : 1. Extraire les "scopes" du JWT et les
 * transformer en autorités que Spring Security peut utiliser. 2. Définir quelle "claim" du JWT doit
 * être utilisée comme nom d'utilisateur principal (le principal).
 */
@Component
@RequiredArgsConstructor
public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
    String principalClaimName = jwt.getClaimAsString("preferred_username");
    return new JwtAuthenticationToken(jwt, authorities, principalClaimName);
  }
}
