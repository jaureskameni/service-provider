package cm.klg.service_provider.domain.ServiceProvider;

public record YearOfExperience(int value) {
  public static YearOfExperience from(int value) {
    return new YearOfExperience(value);
  }
}
