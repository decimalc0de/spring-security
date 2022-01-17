package com.decimalcode.qmed.api._repositories;

import com.decimalcode.qmed.api.users.services.UserPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserPermissionRepository extends JpaRepository<UserPermissions, Long> {

    Optional<UserPermissions> findByPermission(String name);
}
