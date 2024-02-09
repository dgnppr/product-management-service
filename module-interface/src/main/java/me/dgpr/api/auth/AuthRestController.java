package me.dgpr.api.auth;

import me.dgpr.api.ApiResponse;
import me.dgpr.api.auth.dto.LoginRequest;
import me.dgpr.api.auth.dto.LoginResponse;
import me.dgpr.domains.manager.usecase.LoginManagerUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {

    private final LoginManagerUseCase loginManagerUseCase;

    public AuthRestController(final LoginManagerUseCase loginManagerUseCase) {
        this.loginManagerUseCase = loginManagerUseCase;
    }

    @PostMapping("/v1/login")
    public ApiResponse<LoginResponse> login(
            @Validated @RequestBody final LoginRequest request
    ) {
        loginManagerUseCase.query(request.toQuery());
        return ApiResponse.ok(new LoginResponse(null));
    }
}
