package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import cm.klg.service_provider.application.outbound.ProviderClientRepository;
import cm.klg.service_provider.domain.UserId;
import cm.klg.service_provider.domain.provider_client.ProviderClient;
import cm.klg.service_provider.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ProviderClientJpaRepository implements ProviderClientRepository {
  private final ProviderClientSpringRepository providerClientSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public boolean existsByUserIdAndProviderId(UserId userId, ServiceProviderId providerId) {
    return providerClientSpringRepository.existsByUserIdAndProviderId(
        userId.value(), providerId.value());
  }

  @Override
  public void insertIfAbsent(ProviderClient providerClient) {
    ProviderClientJpa providerClientJpa = jpaMapper.toJpa(providerClient);
    int insertedRows =
        providerClientSpringRepository.insertIfAbsent(
            providerClientJpa.getId().toString(),
            providerClientJpa.getUserId().toString(),
            providerClientJpa.getProviderId().toString(),
            providerClientJpa.getCreatedAt());
    if (insertedRows == 0) {
      log.debug(
          "Provider client relation for user {} and provider {} already exists, skipping"
              + " insertion.",
          providerClient.getUserId().value(),
          providerClient.getProviderId().value());
    }
  }
}
