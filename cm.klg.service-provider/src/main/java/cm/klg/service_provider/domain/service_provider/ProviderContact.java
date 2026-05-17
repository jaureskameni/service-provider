package cm.klg.service_provider.domain.service_provider;

import cm.klg.service_provider.domain.PhoneNumber;

public record ProviderContact(UserCityId city, UserDistrictId district, PhoneNumber phoneNumber) {}
