package cm.klg.service_provider.config;

import static cm.klg.service_provider.utils.Constants.REGEX_UUID_WITH_DELIMITER;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String SERVICE_PROVIDER_ID_PATH =
      "/service-provider/{serviceProviderId:%s}".formatted(REGEX_UUID_WITH_DELIMITER);

  private final KeycloakJwtConverter keycloakJwtConverter;

  @Bean
  @Order(0)
  public SecurityFilterChain publicEndpoints(HttpSecurity http) throws Exception {
    return http.securityMatcher(
            "/service-catalog",
            "/service-provider/search",
            SERVICE_PROVIDER_ID_PATH,
            SERVICE_PROVIDER_ID_PATH + "/portfolio",
            SERVICE_PROVIDER_ID_PATH + "/services")
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .build();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain protectedEndpoints(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/service-provider")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/service-provider/services")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/service-provider/portfolio")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/service-provider/portfolio")
                    .authenticated()
                    .requestMatchers(HttpMethod.PUT, "/service-provider/portfolio/*")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/service-provider/portfolio/*")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/service-provider/services")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/service-provider/profile")
                    .authenticated()
                    .requestMatchers(HttpMethod.PUT, "/service-provider/profile")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/service-provider")
                    .hasAuthority(Scopes.SERVICE_PROVIDER_READ_ALL)
                    .requestMatchers(HttpMethod.PUT, SERVICE_PROVIDER_ID_PATH + "/approve")
                    .hasAuthority(Scopes.SERVICE_PROVIDER_APPROVE)
                    .requestMatchers(HttpMethod.PUT, SERVICE_PROVIDER_ID_PATH + "/reject")
                    .hasAuthority(Scopes.SERVICE_PROVIDER_REJECT)
                    .anyRequest()
                    .denyAll())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter)))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }
}
