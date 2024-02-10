package me.dgpr.config.security;

import java.util.Collection;
import java.util.Objects;
import me.dgpr.domains.manager.domain.Manager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class ManagerContext implements UserDetails {

    public static final String ROLE = "ROLE_USER";
    private Manager manager;

    protected ManagerContext(final Manager manager) {
        this.manager = manager;
    }

    public static ManagerContext from(final Manager manager) {
        return new ManagerContext(manager);
    }

    public Manager getManager() {
        return manager;
    }

    public Long getId() {
        Objects.requireNonNull(manager.managerId());
        return manager.managerId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(ROLE);
    }

    @Override
    public String getPassword() {
        return manager.password();
    }

    @Override
    public String getUsername() {
        return String.valueOf(manager.managerId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return manager.deletedAt() == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return manager.deletedAt() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return manager.deletedAt() == null;
    }

    @Override
    public boolean isEnabled() {
        return manager.deletedAt() == null;
    }
}
