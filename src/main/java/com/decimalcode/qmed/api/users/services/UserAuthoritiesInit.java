package com.decimalcode.qmed.api.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserAuthoritiesInit implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserServiceImpl userService;
    private final UserRoleService roleService;
    @Autowired
    public UserAuthoritiesInit(UserServiceImpl userService, UserRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * THE METHOD HELPS US TO CREATE AN INITIAL USER FOR OUR APP
     * @param event
     */
    @Override
    @SuppressWarnings({"JavaDoc", "NullableProblems"})
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        UserPermissions readPermission = roleService.createPermissionIfNotFound("READ_PERMISSION");
        UserPermissions writePermission = roleService.createPermissionIfNotFound("WRITE_PERMISSION");

        UserRole role_user = roleService.createRoleIfNotFound("ROLE_USER", List.of(readPermission));
        UserRole role_admin = roleService.createRoleIfNotFound("ROLE_ADMIN", List.of(readPermission, writePermission));

        UserEntity user = UserEntity.build()
                                    .setUsername("ugo")
                                    .setFullName("Odili Peter")
                                    .setEmail("dev.noreply.smtp@gmail.com")
                                    .setTelephone("07021563262")
                                    .setPassword("benedicta")
                                    .setRoles(Set.of(role_user, role_admin));

        userService.signUp(user);

        alreadySetup = true;
    }

}
