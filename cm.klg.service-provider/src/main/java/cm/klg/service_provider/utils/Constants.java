package cm.klg.service_provider.utils;

public final class Constants {

  private Constants() {}

  public static final String REGEX_UUID_WITH_DELIMITER =
      "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

  public static final class Keycloak {
    private Keycloak() {}

    public static final String RESOURCE_ACCESS = "resource_access";
    public static final String ROLES = "roles";
    public static final String ROLE_PREFIX = "ROLE_";
  }
}
