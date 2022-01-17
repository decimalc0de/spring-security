package com.decimalcode.qmed.api.users.services;

import com.decimalcode.qmed.api._repositories.IUserRoleRepository;
import com.decimalcode.qmed.api.mail.services.utils.EmailServiceImpl;
import com.decimalcode.qmed.api._repositories.IUserRepository;
import com.decimalcode.qmed.exception.custom.ApiException;
import com.decimalcode.qmed.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final UserRoleService roleService;

    @Transactional
    public ApiResponse<UserEntity> signUp(UserEntity user) {
        /*
         * Encoding the password using BCryptPasswordEncoder
         */
        String encodedPwd = passwordEncoder.encode(user.getPassword());
        /*
         * Set encoded password
         */
        user.setPassword(encodedPwd);
        /*
        * Set default user permission
        */
        if(user.getRoles().isEmpty()) {
            UserPermissions readPermission = roleService.createPermissionIfNotFound("READ_PERMISSION");
            UserRole role_user = roleService.createRoleIfNotFound("ROLE_USER", List.of(readPermission));
            user.includeRoles(role_user);
        }
        /*
         * Persisting data in database
         */
        userRepository.save(user);
        /*
         * Send email confirmation token
         */
        return emailService.confirmYourEmailAccount(user);
    }

    @Override
    /*
     * From Spring security
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = getUser(username);
        return new UserPrincipal(user.getUsername(), user.getPassword(), user.getAuthorities(), user);
    }

    @Override
    @Transactional
    public UserEntity persistUser(UserEntity user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public UserEntity getUser(String username) {
        if(Strings.isNotBlank(username)) {
            Optional<UserEntity> optionalUsername = userRepository.findByUsername(username);
            if (optionalUsername.isPresent()) return optionalUsername.get();
        }
        throw new ApiException("User name not found");
    }

    @Override
    @Transactional
    public boolean usernameAlreadyExist(String username) {
        return  userRepository.findByUsername(username).isPresent();
    }

}
