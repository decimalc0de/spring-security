package com.decimalcode.qmed.api.users.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private String role;

    @ManyToMany(mappedBy = "roles")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<UserEntity> users = Set.of();

    @ManyToMany
    @JoinTable(
        name = "roles_permissions",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "permissions_id", referencedColumnName = "id"))
    private Collection<UserPermissions> permissions;

    public UserRole(String role) {
        this.role = role;
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getName(){ return role; }

}
