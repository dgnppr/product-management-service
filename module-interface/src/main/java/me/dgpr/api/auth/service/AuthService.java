package me.dgpr.api.auth.service;

import java.time.Duration;
import java.util.Date;
import me.dgpr.api.auth.dto.LoginRequest;
import me.dgpr.api.auth.dto.LoginResponse;
import me.dgpr.config.security.JwtTokenHandler;
import me.dgpr.config.security.JwtTokenProperties;
import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.usecase.LoginManagerUseCase;
import me.dgpr.domains.manager.usecase.LogoutManagerUseCase;
import me.dgpr.domains.manager.usecase.LogoutManagerUseCase.Command;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final LoginManagerUseCase loginManagerUseCase;
    private final LogoutManagerUseCase logoutManagerUseCase;
    private final JwtTokenHandler jwtTokenHandler;
    private final JwtTokenProperties jwtTokenProperties;

    public AuthService(
            LoginManagerUseCase loginManagerUseCase,
            LogoutManagerUseCase logoutManagerUseCase,
            JwtTokenHandler jwtTokenHandler,
            JwtTokenProperties jwtTokenProperties
    ) {
        this.loginManagerUseCase = loginManagerUseCase;
        this.logoutManagerUseCase = logoutManagerUseCase;
        this.jwtTokenHandler = jwtTokenHandler;
        this.jwtTokenProperties = jwtTokenProperties;
    }

    public LoginResponse login(LoginRequest request) {
        Manager manager = loginManagerUseCase.query(request.toQuery());

        String token = jwtTokenHandler.generateToken(
                manager.id(),
                jwtTokenProperties.getSecretKey(),
                new Date(),
                jwtTokenProperties.getExpirationTime()
        );

        return new LoginResponse(token);
    }

    public void logout(String token) {
        Long managerId = jwtTokenHandler.verifyAndGetIdFromToken(
                jwtTokenProperties.getSecretKey(),
                token
        );

        Command command = new Command(
                managerId,
                token,
                Duration.ofMillis(jwtTokenProperties.getExpirationTime())
        );

        logoutManagerUseCase.command(command);
    }
}
