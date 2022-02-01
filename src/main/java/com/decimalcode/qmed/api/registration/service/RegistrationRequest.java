package com.decimalcode.qmed.api.registration.service;

import com.decimalcode.qmed.validators.email.UserEmail;
import com.decimalcode.qmed.validators.member.UserName;
import com.decimalcode.qmed.validators.password.UserPassword;
import com.decimalcode.qmed.validators.telephone.UserContact;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class RegistrationRequest {
    @UserName
    private String username;

    @NotBlank
    private String fullName;

    @UserEmail
    private String email;

    @UserContact
    private String telephone;

    @UserPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
