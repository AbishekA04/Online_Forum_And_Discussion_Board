package com.example.forum_app.repository;

import com.example.forum_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;  // Added this import
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Existing: Case-insensitive username search (for login)
    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(?1)")
    Optional<User> findByUsername(String username);

    // Existing: Case-insensitive email search (for registration)
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    Optional<User> findByEmail(String email);

    // Additional Queries for Flexibility

    // Find by exact username (case-sensitive, if needed)
    Optional<User> findByUsernameIgnoreCase(String username);

    // Find by exact email (case-sensitive)
    Optional<User> findByEmailIgnoreCase(String email);

    // Find users by role
    List<User> findByRole(String role);

    // Find users by role (case-insensitive)
    @Query("SELECT u FROM User u WHERE LOWER(u.role) = LOWER(?1)")
    List<User> findByRoleIgnoreCase(String role);

    // Find users whose username contains a substring (partial match)
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<User> findByUsernameContainingIgnoreCase(String partialUsername);

    // Find users whose email contains a substring
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<User> findByEmailContainingIgnoreCase(String partialEmail);

    // Count users by role
    long countByRole(String role);

    // Check if username exists (for validation)
    boolean existsByUsername(String username);

    // Check if email exists (for validation)
    boolean existsByEmail(String email);

    // Find all users ordered by username
    List<User> findAllByOrderByUsernameAsc();

    // Find users by role and username containing
    @Query("SELECT u FROM User u WHERE LOWER(u.role) = LOWER(?1) AND LOWER(u.username) LIKE LOWER(CONCAT('%', ?2, '%'))")
    List<User> findByRoleAndUsernameContainingIgnoreCase(String role, String partialUsername);

    // Custom query with parameters (example: find by role and email domain)
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.email LIKE %:domain")
    List<User> findByRoleAndEmailDomain(@Param("role") String role, @Param("domain") String domain);

    // Delete by username (if needed, but use with caution)
    void deleteByUsername(String username);

    // Find users created after a certain date (assuming you add createdAt to User model)
    // @Query("SELECT u FROM User u WHERE u.createdAt > ?1")
    // List<User> findByCreatedAtAfter(LocalDateTime date);
    // Uncomment if you add createdAt to User.java
}
