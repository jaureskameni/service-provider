package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.ServiceProviderRepository;
import cm.klg.service_provider.application.outbound.ServiceTypeRepository;
import cm.klg.service_provider.application.outbound.UserRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(
    basePackages = {
      "cm.klg.service_provider.adapter.persistence.outbound.jpa",
    })
@EnableJpaRepositories(basePackages = {"cm.klg.service_provider.adapter.persistence.outbound.jpa"})
public class JpaSpringBeans {

  @Bean
  public UserRepository userRepository(
      UserSpringRepository userSpringRepository, JpaMapper jpaMapper) {
    return new UserJpaRepository(userSpringRepository, jpaMapper);
  }

  @Bean
  public ServiceProviderRepository serviceProviderRepository(
      ServiceProviderSpringRepository serviceProviderSpringRepository, JpaMapper jpaMapper) {
    return new ServiceProviderJpaRepository(serviceProviderSpringRepository, jpaMapper);
  }

  @Bean
  public ServiceTypeRepository serviceTypeRepository(
      ServiceTypeSpringRepository serviceTypeSpringRepository, JpaMapper jpaMapper) {
    return new ServiceProviderServiceTypeJpaRepository(serviceTypeSpringRepository, jpaMapper);
  }
}
