package com.decimalcode.qmed.api.__default;

import com.decimalcode.qmed.api.users.service.UserEntity;
import com.decimalcode.qmed.api.users.service.UserRole;
import com.decimalcode.qmed.api.users.service.UserRoleService;
import com.decimalcode.qmed.api.users.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Use the class to register default users
 */
@Component
public class DefaultUsers implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserServiceImpl userService;
    private final UserRoleService roleService;

    @Autowired
    public DefaultUsers(UserServiceImpl userService,
                        UserRoleService roleService) {
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
        /*
         * Define user role
         */
        UserRole role_user = roleService.getRole("ROLE_USER");
        UserRole role_admin = roleService.getRole("ROLE_ADMIN");
        /*
         * Define default user
         */
        if(allRolesExist(role_user, role_admin)) {
            UserEntity user = UserEntity.build()
                    .setUsername("ugo")
                    .setFullName("Odili Peter")
                    .setEmail("dev.noreply.smtp@gmail.com")
                    .setTelephone("07021563262")
                    .setPassword("benedicta")
                    .setRoles(Set.of(role_user, role_admin));
            userService.signUp(user);
        }

        alreadySetup = true;
    }

    private boolean allRolesExist(UserRole... roles) {
        for(UserRole role: roles)
            if(role == null) return false;
        return true;
    }

}
