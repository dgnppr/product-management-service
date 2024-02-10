package me.dgpr.api.auth;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import me.dgpr.api.ApiResponse;
import me.dgpr.api.auth.dto.LoginRequest;
import me.dgpr.api.auth.dto.LoginResponse;
import me.dgpr.api.auth.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/v1/login")
    public ApiResponse<LoginResponse> login(
            @Validated @RequestBody final LoginRequest request
    ) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/v1/logout")
    public ApiResponse<Void> logout(
            @RequestHeader(AUTHORIZATION) final String token
    ) {
        authService.logout(token);
        return ApiResponse.ok();
    }
}
