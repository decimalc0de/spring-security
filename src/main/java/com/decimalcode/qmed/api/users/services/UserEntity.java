package com.decimalcode.qmed.api.users.services;

import com.decimalcode.qmed.security.JwtToken;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name="Users")
@RequiredArgsConstructor
@Accessors(chain = true)
@SuppressWarnings({"FieldMayBeFinal", "unused", "UnusedReturnValue"})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private String username;

    @Column()
    @JsonProperty("original-name")
    private String fullName;

    @Column()
    private String email;

    @Column()
    private String telephone;

    @Column()
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Column()
    @JsonProperty("is-verified")
    private boolean enabled = false;

    @Column()
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean locked = false;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",
                orphanRemoval = true,
                cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<JwtToken> accessTokens = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "id"))
    private Set<UserRole> roles = new LinkedHashSet<>();

    /*
     * User Builder
     */
    public static UserEntity build(){
        return new UserEntity();
    }

    public void includeToken(JwtToken accessToken) {
        if(accessToken != null) {
            getAccessTokens().add(accessToken);
            accessToken.setUser(this);
        }
    }

    @SuppressWarnings("unused")
    public void removeToken(JwtToken accessToken) {
        if(accessToken != null) {
            getAccessTokens().remove(accessToken);
            accessToken.setUser(null);
        }
    }

    @SuppressWarnings("unused")
    public void includeRoles(UserRole role) {
        if(role != null) {
            getRoles().add(role);
        }
    }

    @SuppressWarnings("unused")
    public void removeRoles(UserRole role) {
        if(role != null) {
            getRoles().remove(role);
        }
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authorityList = new LinkedList<>();
        for(UserRole role: getRoles()) {
            authorityList.add(role.getRole());
            role.getPermissions().forEach(
                    (permission) -> authorityList.add(permission.getName())
            );
        }
        return authorityList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Map<String, List<String>> getRolePermission() {
        Map<String, List<String>> rolePermission = new LinkedHashMap<>();
        for(UserRole role: getRoles()) {
            List<String> permissions = new LinkedList<>();
            role.getPermissions().forEach(
                    (permission) -> permissions.add(permission.getName())
            );
            rolePermission.put(role.getName(), permissions);
        }
        return rolePermission;
    }

}
