package cm.klg.service_provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jk-dev.client-credentials")
public record ClientCredentialsProperties(String clientId) {}
