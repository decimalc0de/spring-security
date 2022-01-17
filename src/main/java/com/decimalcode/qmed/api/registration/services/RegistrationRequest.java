package com.decimalcode.qmed.api.registration.services;

import com.decimalcode.qmed.api.users.services.UserRole;
import com.decimalcode.qmed.validators.EmailValidation;
import com.decimalcode.qmed.validators.TelephoneValidation;
import com.decimalcode.qmed.validators.member.MemberAlreadyExist;
import com.decimalcode.qmed.validators.password.PasswordStrength;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.GsonBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class RegistrationRequest {
    @MemberAlreadyExist
    private String username;

    @NotBlank
    private String fullName;

    @EmailValidation
    private String email;

    @TelephoneValidation
    private String telephone;

    @PasswordStrength
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
