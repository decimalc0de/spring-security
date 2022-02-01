package com.decimalcode.qmed.api.users.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal extends User implements UserDetails {
    private final UserEntity user;
    public UserPrincipal(String username,
                         String password,
                         Collection<? extends GrantedAuthority> authorities,
                         UserEntity user) {
        super(username, password, authorities);
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !user.isEnabled();
    }

}
