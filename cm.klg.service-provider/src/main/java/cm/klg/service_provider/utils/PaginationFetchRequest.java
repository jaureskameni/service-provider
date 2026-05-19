package cm.klg.service_provider.utils;

import lombok.Builder;

@Builder
public record PaginationFetchRequest(int limit, int pageIndex) {}
