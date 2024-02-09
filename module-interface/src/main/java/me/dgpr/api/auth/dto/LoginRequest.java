package me.dgpr.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.dgpr.domains.manager.usecase.LoginManagerUseCase;

public record LoginRequest(
        @NotBlank(message = "휴대폰 번호는 필수입니다.")
        @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 11자리 숫자로만 입력해주세요.")
        String phoneNumber,
        
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        String password
) {

    public LoginManagerUseCase.Query toQuery() {
        return new LoginManagerUseCase.Query(
                phoneNumber,
                password
        );
    }
}
