package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Date;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by its username and password
     * @param username
     * @param password
     * @return
     */
    Optional<User> findUserByUsernameAndPassword(String username, String password);

    /**
     * Find a user by its validation
     * @param validationCode UUID
     * @return list of user (should be max length of 1)
     */
    List<User> findUsersByValidationCode(UUID validationCode);

    /**
     * Find a user that match a specific string in his names
     * @param query the string
     * @return a list of users that match the query
     */
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<User> findByFirstnameLastnameOrUsernameContainingIgnoreCase(String query);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "u.isMale = :gender")
    List<User> findByFirstnameLastnameOrUsernameAndGender(String query, Boolean gender);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "u.birthdate = :birthdate")
    List<User> findByFirstnameLastnameOrUsernameAndBirthdate(String query, String birthdate);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "u.isMale = :gender AND " +
            "u.birthdate = :birthdate")
    List<User> findByFirstnameLastnameOrUsernameAndGenderAndBirthdate(String query, Boolean gender, String birthdate);

    /**
     * Check if a username already exists
     * @param username the username
     * @return true if the username exists
     */
    boolean existsByUsername(String username);
}
