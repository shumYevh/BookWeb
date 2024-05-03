package org.example.bookweb.repository;

import org.example.bookweb.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByRole(Role.RoleName roleName);
}
