package me.dgpr.api.auth.service;

import java.util.Date;
import me.dgpr.api.auth.dto.LoginRequest;
import me.dgpr.api.auth.dto.LoginResponse;
import me.dgpr.config.security.JwtTokenHandler;
import me.dgpr.config.security.JwtTokenProperties;
import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.usecase.LoginManagerUseCase;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final LoginManagerUseCase loginManagerUseCase;
    private final JwtTokenHandler jwtTokenHandler;
    private final JwtTokenProperties jwtTokenProperties;

    public AuthService(
            LoginManagerUseCase loginManagerUseCase,
            JwtTokenHandler jwtTokenHandler,
            JwtTokenProperties jwtTokenProperties
    ) {
        this.loginManagerUseCase = loginManagerUseCase;
        this.jwtTokenHandler = jwtTokenHandler;
        this.jwtTokenProperties = jwtTokenProperties;
    }

    public LoginResponse login(
            LoginRequest request
    ) {
        Manager manager = loginManagerUseCase.query(request.toQuery());

        String token = jwtTokenHandler.generateToken(
                manager.id(),
                jwtTokenProperties.getSecretKey(),
                new Date(),
                jwtTokenProperties.getExpirationTime()
        );

        return new LoginResponse(token);
    }
}
