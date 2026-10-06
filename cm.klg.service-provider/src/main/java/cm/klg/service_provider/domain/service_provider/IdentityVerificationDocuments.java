package cm.klg.service_provider.domain.service_provider;

import org.jspecify.annotations.Nullable;

public record IdentityVerificationDocuments(
    @Nullable CniRectoMediaId cniRectoId, @Nullable CniVersoMediaId cniVersoId) {}
