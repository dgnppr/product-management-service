package me.dgpr.api.product;

import me.dgpr.api.ApiResponse;
import me.dgpr.api.product.dto.CreateProductRequest;
import me.dgpr.config.security.CurrentManager;
import me.dgpr.config.security.ManagerContext;
import me.dgpr.domains.product.usecase.CreateProductUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final CreateProductUseCase createProductUseCase;

    public ProductRestController(CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = createProductUseCase;
    }

    @PostMapping("/v1/stores/{storeId}/products")
    public ApiResponse<Void> createProduct(
            @CurrentManager final ManagerContext managerContext,
            @PathVariable("storeId") final long storeId,
            @Validated @RequestBody final CreateProductRequest request
    ) {
        createProductUseCase.command(request.toCommand(
                managerContext.getId(),
                storeId
        ));
        return ApiResponse.created();
    }
}
