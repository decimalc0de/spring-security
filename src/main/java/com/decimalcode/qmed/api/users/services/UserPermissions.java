package com.decimalcode.qmed.api.users.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPermissions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @SuppressWarnings("FieldCanBeLocal")
    private String permission;

    @ManyToMany(mappedBy = "permissions")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<UserRole> roles;

    public UserPermissions(String permission) {
        this.permission = permission;
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getName(){ return permission; }

}
