package me.dgpr.api.manager.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.dgpr.domains.manager.usecase.CreateManagerUseCase;

public record CreateManagerRequest(
        @NotEmpty(message = "휴대폰 번호는 필수입니다.")
        @Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호는 '010'으로 시작하는 11자리 숫자로만 입력해주세요.")
        String phoneNumber,

        @NotEmpty(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        String password
) {

    public CreateManagerUseCase.Command toCommand() {
        return new CreateManagerUseCase.Command(
                phoneNumber,
                password
        );
    }
}
