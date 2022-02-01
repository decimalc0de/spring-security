package com.decimalcode.qmed.api._repositories;

import com.decimalcode.qmed.api.users.service.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, String> {
    @Transactional
    @Query(findUsernameIgnoreCase)
    Optional<UserEntity> findByUsername(String username);

    String findUsernameIgnoreCase = "select u from UserEntity u where lower(u.username) = lower(?1)";
}
