package com.decimalcode.qmed.api.__default;

import com.decimalcode.qmed.api.users.service.UserPermissions;
import com.decimalcode.qmed.api.users.service.UserRole;
import com.decimalcode.qmed.api.users.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Use the class to register default users roles
 */
@Component
public class DefaultUserRole implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserRoleService roleService;

    @Autowired
    public DefaultUserRole(UserRoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * THE METHOD HELPS US TO CREATE AN INITIAL USER ROLE FOR OUR APP
     * @param event
     */
    @Override
    @SuppressWarnings({"JavaDoc", "NullableProblems"})
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        /*
         * USER ROLE PERMISSION's
         */
        UserPermissions readPermission = roleService.createPermissionIfNotFound("READ_PERMISSION");
        UserPermissions writePermission = roleService.createPermissionIfNotFound("WRITE_PERMISSION");
        /*
         * USER ROLE
         */
        roleService.createRoleIfNotFound("ROLE_ADMIN", List.of(readPermission, writePermission));
        roleService.createRoleIfNotFound("ROLE_USER", List.of(readPermission));

        alreadySetup = true;
    }

}
