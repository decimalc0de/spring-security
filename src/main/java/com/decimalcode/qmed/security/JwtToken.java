package com.decimalcode.qmed.security;

import com.decimalcode.qmed.api.users.services.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.SecretKey;
import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import static com.decimalcode.qmed.misc.ApiGeneralSettings.JWT_SECRET_KEY;
import static com.decimalcode.qmed.misc.ApiGeneralSettings.TOKEN_EXPIRATION_TIME;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "json_web_token")
public class JwtToken implements Serializable {
    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonBackReference
    @JoinColumn(name = "users_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(name = "issued_at",
            columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime issuedAt = OffsetDateTime.now();

    @Column(name = "expiry_at",
            columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime expireAt = OffsetDateTime.now().plusMinutes(TOKEN_EXPIRATION_TIME);

    @Column(name = "unique_request_id")
    private int uniqueRequestId = 0;

    public void autoUniqueRequestId() {
        this.uniqueRequestId++;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JwtToken){
            JwtToken oPayload = (JwtToken) obj;
            return this.id != null && oPayload.id != null && oPayload.id.equals(this.id);
        }
        return false;
    }

    @Transient
    public String generateToken() {
        /*
         * Initialise secret key
         */
        SecretKey jwtSecretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());
        /*
         * Expire time
         */
        long milliSec = OffsetDateTime.now()
                                        .plusMinutes(TOKEN_EXPIRATION_TIME)
                                        .toInstant()
                                        .toEpochMilli();
        /*
         * Sign & Return Token
         */
        return Jwts.builder()
                    .setId(getId().toString())
                    .setSubject(getUser().getUsername())
                    .claim("req-id", getUniqueRequestId())
                    .claim("authorities", getUser().getRolePermission())
                    .setIssuedAt(new Date(getIssuedAt().toInstant().toEpochMilli()))
                    .setExpiration(new Date(milliSec))
                    .signWith(jwtSecretKey)
                    .compact();
    }


}
