package com.decimalcode.qmed.api.token;

import javax.persistence.*;

import com.decimalcode.qmed.api.users.services.UserEntity;
import com.google.gson.JsonObject;
import com.decimalcode.qmed.api.IdGenerator;
import org.hibernate.annotations.GenericGenerator;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@SuppressWarnings("unused")
@Table(name = "AuthToken")
public class TokenEntity implements Serializable {
    @Transient
    private final int DEFAULT_EXPIRATION = 15; // in minute i.e default 15 minute
    @Id
    @GenericGenerator(
        name = "entityId",
        strategy = "com.decimalcode.qmed.api.IdGenerator",
        parameters = {
            @Parameter(name = IdGenerator.INCREMENT_PARAM, value = "50"),
            @Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "ID_"),
            @Parameter(name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%010d")
        }
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "entityId"
    )
    @Column(name = "entityId")
    private String id;

    @NotBlank
    private String token;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity appUser;

    @Column(name = "expiry_time",
            columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime expiryTime;

    public TokenEntity() {
        super();
    }

    public TokenEntity(final UserEntity appUser, final String token) {
        super();
        this.token = token;
        this.appUser = appUser;
        this.expiryTime = calculateExpiryTime(DEFAULT_EXPIRATION);
    }

    public TokenEntity(final UserEntity appUser, final String token, final int expiration) {
        super();
        this.token = token;
        this.appUser = appUser;
        this.expiryTime = calculateExpiryTime(expiration);
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public UserEntity getAppUser() {
        return appUser;
    }

    public void setAppUser(UserEntity appUser) {
        this.appUser = appUser;
    }

    public OffsetDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(final OffsetDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    private OffsetDateTime calculateExpiryTime(final int expiryTimeInMinutes) {
        final OffsetDateTime expireTime = OffsetDateTime.now();
        return expireTime.plusMinutes(expiryTimeInMinutes);
    }

    //

    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("user", appUser.toString());
        json.addProperty("token", token);
        json.addProperty("expirationTime", expiryTime.toString());
        return json.toString();
    }

}
