package com.decimalcode.qmed.api.token;

import com.decimalcode.qmed.api.users.services.UserEntity;
import com.decimalcode.qmed.api.users.services.UserServiceImpl;
import com.decimalcode.qmed.misc.ApplicationContextImpl;
import org.springframework.stereotype.Component;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Component
@NoArgsConstructor
public class TokenDto {
    private String token;
    private String username;

    public String get(){
        return token;
    }

    public UserEntity getUser(){
        return ApplicationContextImpl.getBean(UserServiceImpl.class).getUser(username);
    }

}
