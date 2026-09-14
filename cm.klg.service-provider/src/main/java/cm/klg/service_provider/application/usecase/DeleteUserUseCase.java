package cm.klg.service_provider.application.usecase;

import cm.klg.service_provider.application.outbound.UserRepository;
import cm.klg.service_provider.domain.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCase {
  private final UserRepository userRepository;

  public void execute(UserId userId) {
    userRepository.deleteById(userId);
  }
}
