package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.security.property.AccountState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @implNote Full implementation of {@link UserDetails}
 */
@Entity
@Getter
@Setter
@Table(name = "accounts")
public class Account implements UserDetails {

    public Account() {
    }

    public Account(String email,
                   String password,
                   Role primaryRole,
                   AccountState state) {
        this.email = email;
        this.password = password;
        this.roles.add(primaryRole);
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated
    @Column(name = "state", nullable = false)
    private AccountState state;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "accounts_roles",
            joinColumns = @JoinColumn(name = "id_account", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "accounts_authorities",
            joinColumns = @JoinColumn(name = "id_account", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_privilege", referencedColumnName = "id"))
    private Set<Authority> authorities = new HashSet<>();

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return getState().equals(AccountState.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return getState().getValue() >= AccountState.SANCTIONED.getValue();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getState().getValue() >= AccountState.EXPIRED_PASSWORD.getValue();
    }

    @Override
    public boolean isEnabled() {
        return getState().getValue() >= AccountState.DISABLED.getValue();
    }

    @Override
    public Set<Authority> getAuthorities() {
        return Stream.concat(
                        roles.stream()
                                .map(Role::getAuthorities)
                                .flatMap(Collection::stream),
                        authorities.stream())
                .collect(Collectors.toSet());
    }
}
