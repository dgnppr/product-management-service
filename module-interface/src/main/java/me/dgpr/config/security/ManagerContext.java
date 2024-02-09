package me.dgpr.config.security;

import java.util.Collection;
import me.dgpr.domains.manager.domain.Manager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class ManagerContext implements UserDetails {

    public static final String ROLE = "ROLE_USER";
    private Manager manager;

    protected ManagerContext(Manager manager) {
        this.manager = manager;
    }

    public static ManagerContext from(Manager manager) {
        return new ManagerContext(manager);
    }

    public Manager getManager() {
        return manager;
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
        return manager.phoneNumber();
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
