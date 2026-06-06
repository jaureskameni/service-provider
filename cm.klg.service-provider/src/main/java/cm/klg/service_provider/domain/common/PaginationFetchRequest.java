package cm.klg.service_provider.domain.common;

import cm.klg.service_provider.domain.service_provider.InvalidServiceProviderDataException;
import lombok.Builder;

@Builder
public record PaginationFetchRequest(int limit, int pageIndex) {
  public PaginationFetchRequest {
    if (limit < 10 || pageIndex < 0) {
      throw new InvalidServiceProviderDataException();
    }
  }
}
