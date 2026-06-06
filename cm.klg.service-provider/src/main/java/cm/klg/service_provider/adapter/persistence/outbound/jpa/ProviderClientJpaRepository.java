package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.provider_client.ProviderClient;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProviderClientJpaRepository implements ProviderClientRepository {
  private final ProviderClientSpringRepository providerClientSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public boolean existsByUserIdAndProviderId(UserId userId, ServiceProviderId providerId) {
    return providerClientSpringRepository.existsByUserIdAndProviderId(userId.value(), providerId.value());
  }

  @Override
  public void insert(ProviderClient providerClient) {
    providerClientSpringRepository.save(jpaMapper.toJpa(providerClient));
  }
}
