package org.example.bookweb.repository.user;

import java.util.Optional;
import org.example.bookweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findAllByEmail(String email);
}
