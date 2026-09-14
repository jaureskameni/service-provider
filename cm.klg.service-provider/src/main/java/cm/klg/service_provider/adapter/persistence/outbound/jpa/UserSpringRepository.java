package cm.klg.service_provider.adapter.persistence.outbound.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSpringRepository extends JpaRepository<UserJpa, UUID> {
  @Query("SELECT u FROM UserJpa u WHERE u.id = :id")
  Optional<UserJpa> findById(@Param("id") UUID id);

  @Modifying
  @Query(
      value =
          "INSERT INTO t_user (c_id, c_firstname, c_lastname, c_email_address,"
              + " c_phone_number, c_created_at) VALUES (:id, :firstname, :lastname,"
              + " :emailAddress, :phoneNumber, :createdAt) ON CONFLICT (c_id) DO NOTHING",
      nativeQuery = true)
  int insertIfAbsent(
      @Param("id") UUID id,
      @Param("firstname") String firstname,
      @Param("lastname") String lastname,
      @Param("emailAddress") String emailAddress,
      @Param("phoneNumber") String phoneNumber,
      @Param("createdAt") LocalDateTime createdAt);

  List<UserJpa> findAllByIdIn(List<UUID> uuids);
}
