package cm.klg.service_provider.domain.service_provider;

import org.jspecify.annotations.Nullable;

public record ProviderProfile(ProviderContact contact, @Nullable AboutProvider about) {}
