package cm.klg.service_provider.domain.service_provider;

public record YearOfExperience(int value) {
  public YearOfExperience {
    if (value < 0) {
      throw new InvalidServiceProviderPaginationDataException();
    }
  }

  public static YearOfExperience from(int value) {
    return new YearOfExperience(value);
  }
}
