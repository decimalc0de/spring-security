package com.decimalcode.qmed.api._repositories;

import com.decimalcode.qmed.api.token.TokenEntity;
import com.decimalcode.qmed.api.users.services.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<TokenEntity, String> {
    @Modifying
    @Query(DELETE_ALL_SQL)
    void deleteAllExpiredToken(OffsetDateTime now);
    @Query(value = VERIFY_TOKEN_SQL)
    Optional<TokenEntity> findFirstByAppUserAndTokenAndExpiryTime(UserEntity user, String token, OffsetDateTime expiryTime);

    String DELETE_ALL_SQL = "delete from TokenEntity t where t.expiryTime <= ?1";
    String VERIFY_TOKEN_SQL = "select t from TokenEntity t where t.appUser = ?1 and t.token = ?2 and t.expiryTime >= ?3";
}
