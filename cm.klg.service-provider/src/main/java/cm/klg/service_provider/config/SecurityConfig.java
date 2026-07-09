package cm.klg.service_provider.config;

import static cm.klg.service_provider.utils.Constants.REGEX_UUID_WITH_DELIMITER;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final KeycloakJwtConverter keycloakJwtConverter;

  @Bean
  @Order(0)
  public SecurityFilterChain publicEndpoints(HttpSecurity http) throws Exception {
    return http.securityMatcher(
            "/service-catalog",
            "/service-provider/search",
            "/service-provider/{serviceProviderId:%s}".formatted(REGEX_UUID_WITH_DELIMITER))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .build();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain protectedEndpoints(HttpSecurity http) {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/service-provider")
                    .authenticated()
                    .requestMatchers(HttpMethod.PUT, "/service-provider/add-service")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/service-provider")
                    .hasAnyAuthority(Scopes.SERVICE_PROVIDER_READ_ALL)
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/service-provider/{serviceProviderId:%s}/approve"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .hasAnyAuthority(Scopes.SERVICE_PROVIDER_APPROVE)
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/service-provider/{serviceProviderId:%s}/reject"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .hasAnyAuthority(Scopes.SERVICE_PROVIDER_REJECT)
                    .requestMatchers(
                        HttpMethod.GET,
                        "/service-provider/{serviceProviderId:%s}/profile"
                            .formatted(REGEX_UUID_WITH_DELIMITER))
                    .authenticated()
                    .anyRequest()
                    .denyAll())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter)))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }
}
