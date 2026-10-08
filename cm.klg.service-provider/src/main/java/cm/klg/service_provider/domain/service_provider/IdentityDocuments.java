package cm.klg.service_provider.domain.service_provider;

public record IdentityDocuments(CniRectoMediaId cniRectoId, CniVersoMediaId cniVersoId) {
  public static IdentityDocuments of(CniRectoMediaId cniRectoId, CniVersoMediaId cniVersoId) {
    return new IdentityDocuments(cniRectoId, cniVersoId);
  }
}
