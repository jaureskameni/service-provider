package cm.klg.service_provider;

import cm.klg.service_provider.config.ClientCredentialsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
  ClientCredentialsProperties.class,
})
public class ServiceProviderApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceProviderApplication.class, args);
  }
}
