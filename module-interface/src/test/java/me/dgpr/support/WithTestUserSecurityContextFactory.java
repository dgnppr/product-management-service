package me.dgpr.support;


import java.time.LocalDateTime;
import me.dgpr.config.security.ManagerContext;
import me.dgpr.domains.manager.domain.Manager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithTestUserSecurityContextFactory implements
        WithSecurityContextFactory<WithTestUser> {

    @Override
    public SecurityContext createSecurityContext(WithTestUser annotation) {
        Manager manager = new Manager(
                annotation.id(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                "01012345678",
                "password"
        );

        ManagerContext managerContext = ManagerContext.from(manager);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                managerContext,
                managerContext.getPassword(),
                managerContext.getAuthorities()
        );

        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
