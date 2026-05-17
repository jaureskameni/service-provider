package cm.klg.service_provider.domain.service_provider;

import cm.klg.common.base.domain.CreatedAt;
import org.jspecify.annotations.Nullable;

public record ProviderAudit(CreatedAt createdAt, @Nullable CreatedAt updatedAt) {}
