package me.dgpr.api.store.dto;

import jakarta.validation.constraints.NotBlank;
import me.dgpr.domains.store.usecase.CreateStoreUseCase.Command;

public record CreateStoreRequest(
        @NotBlank(message = "사업자 등록 번호는 필수입니다.")
        String companyRegistrationNumber,

        @NotBlank(message = "상호명은 필수입니다.")
        String businessName
) {

    public Command toCommand(final long managerId) {
        return new Command(
                managerId,
                companyRegistrationNumber,
                businessName
        );
    }
}
