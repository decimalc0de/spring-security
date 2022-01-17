package com.decimalcode.qmed.api.users.services;

public interface IUserService {
    UserEntity getUser(String username);
    UserEntity persistUser(UserEntity user);
    boolean usernameAlreadyExist(String username);

}
