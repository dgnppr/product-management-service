package me.dgpr.api.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import me.dgpr.domains.product.usecase.UpdateProductUseCase.Command;

public record UpdateProductRequest(
        @NotNull(message = "가격은 필수입니다.")
        @DecimalMin(value = "0.0", message = "가격은 0 이상이어야 합니다.")
        BigDecimal price,

        @NotNull(message = "원가는 필수입니다.")
        @DecimalMin(value = "0.0", message = "가격은 0 이상이어야 합니다.")
        BigDecimal cost,

        @NotBlank(message = "상품 이름은 필수이며, 비어 있을 수 없습니다.")
        @Size(min = 1, max = 50, message = "상품 이름은 1자 이상, 50자 이하이어야 합니다.")
        String name,

        @Size(max = 100, message = "상품 설명은 100자 이하이어야 합니다.")
        String description,

        @NotBlank(message = "바코드는 필수이며, 비어 있을 수 없습니다.")
        @Size(max = 100, message = "바코드는 100자 이하이어야 합니다.")
        String barcode,

        @NotNull(message = "유통기한은 필수입니다.")
        LocalDateTime expirationDate,

        @NotNull(message = "사이즈는 필수입니다. (허용값. SMALL, LARGE)")
        String size,

        @NotNull(message = "카테고리 ID 목록은 필수입니다.")
        @Size(min = 1, message = "적어도 하나의 카테고리 ID가 필요합니다.")
        Set<@NotNull(message = "카테고리 ID는 null일 수 없습니다.") Long> categoryIds
) {

    public Command toCommand(
            final long productId,
            final long managerId,
            final long storeId
    ) {
        return new Command(
                productId,
                managerId,
                storeId,
                price,
                cost,
                name,
                description,
                barcode,
                expirationDate,
                size,
                categoryIds
        );
    }

}
