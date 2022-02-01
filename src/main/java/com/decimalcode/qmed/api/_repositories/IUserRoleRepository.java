package com.decimalcode.qmed.api._repositories;

import com.decimalcode.qmed.api.users.service.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(String name);
}
