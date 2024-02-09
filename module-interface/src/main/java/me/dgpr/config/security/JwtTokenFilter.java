package me.dgpr.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.usecase.QueryManagerByIdUseCase;
import me.dgpr.domains.manager.usecase.QueryManagerByIdUseCase.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final JwtTokenHandler jwtTokenHandler;
    private final QueryManagerByIdUseCase queryManagerByIdUseCase;

    public JwtTokenFilter(
            String secretKey,
            JwtTokenHandler jwtTokenHandler,
            QueryManagerByIdUseCase queryManagerByIdUseCase
    ) {
        this.secretKey = secretKey;
        this.jwtTokenHandler = jwtTokenHandler;
        this.queryManagerByIdUseCase = queryManagerByIdUseCase;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (Objects.isNull(token)) {
                chain.doFilter(
                        request,
                        response
                );
                return;
            }

            Long managerId = jwtTokenHandler.verifyAndGetIdFromToken(
                    secretKey,
                    token
            );
            Manager manager = queryManagerByIdUseCase.query(new Query(managerId));
            setAuthentication(ManagerContext.from(manager));

        } catch (Exception e) {
            chain.doFilter(
                    request,
                    response
            );
            return;
        }
        
        chain.doFilter(
                request,
                response
        );
    }

    private void setAuthentication(ManagerContext context) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                context,
                context.getPassword(),
                context.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
