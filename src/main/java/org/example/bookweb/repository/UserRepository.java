package org.example.bookweb.repository;

import java.util.Optional;
import org.example.bookweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
}
