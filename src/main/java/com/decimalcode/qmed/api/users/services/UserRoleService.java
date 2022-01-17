package com.decimalcode.qmed.api.users.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.decimalcode.qmed.api._repositories.IUserPermissionRepository;
import com.decimalcode.qmed.api._repositories.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserRoleService {
    private final IUserRoleRepository roleRepository;
    private final IUserPermissionRepository permissionRepository;

    @Autowired
    public UserRoleService(IUserRoleRepository roleRepository,
                           IUserPermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public UserPermissions createPermissionIfNotFound(String name) {
        Optional<UserPermissions> optionalPermission = permissionRepository.findByPermission(name);
        if (optionalPermission.isEmpty()) {
            UserPermissions permissions = new UserPermissions(name);
            return permissionRepository.save(permissions);
        }
        return optionalPermission.get();
    }

    @Transactional
    public UserRole createRoleIfNotFound(String name, Collection<UserPermissions> permissions) {
        Optional<UserRole> optionalRole = roleRepository.findByRole(name);
        if (optionalRole.isEmpty()) {
            UserRole role = new UserRole(name);
            role.setPermissions(permissions);
            return roleRepository.save(role);
        }
        return optionalRole.get();
    }

}
