package me.dgpr.api.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import me.dgpr.domains.category.usecase.CreateCategoryUseCase.Command;

public record CreateCategoryRequest(
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        @Size(max = 20, message = "카테고리 이름은 20자 이내로 입력해주세요.")
        String name
) {

    public Command toCommand(
            long managerId,
            long storeId
    ) {
        return new Command(
                managerId,
                storeId,
                name
        );
    }
}
