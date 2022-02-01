package com.decimalcode.qmed.api.users.service;

public interface IUserService {
    UserEntity getUser(String username);
    UserEntity persistUser(UserEntity user);
    boolean usernameAlreadyExist(String username);

}
