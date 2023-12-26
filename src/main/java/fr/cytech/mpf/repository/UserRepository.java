package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    List<User> findUsersByValidationCode(UUID validationCode);
}
