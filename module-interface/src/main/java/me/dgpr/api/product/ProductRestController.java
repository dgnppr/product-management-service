package me.dgpr.api.product;

import me.dgpr.api.ApiResponse;
import me.dgpr.api.product.dto.CreateProductRequest;
import me.dgpr.api.product.dto.UpdateProductRequest;
import me.dgpr.config.security.CurrentManager;
import me.dgpr.config.security.ManagerContext;
import me.dgpr.domains.product.usecase.CreateProductUseCase;
import me.dgpr.domains.product.usecase.DeleteProductUseCase;
import me.dgpr.domains.product.usecase.DeleteProductUseCase.Command;
import me.dgpr.domains.product.usecase.UpdateProductUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductRestController(
            CreateProductUseCase createProductUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
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

    @PostMapping("/v1/stores/{storeId}/products/{productId}/update")
    public ApiResponse<Void> updateProduct(
            @CurrentManager final ManagerContext managerContext,
            @PathVariable("storeId") final long storeId,
            @PathVariable("productId") final long productId,
            @Validated @RequestBody final UpdateProductRequest request
    ) {
        updateProductUseCase.command(
                request.toCommand(
                        productId,
                        managerContext.getId(),
                        storeId
                )
        );
        return ApiResponse.ok();
    }

    @PostMapping("/v1/stores/{storeId}/products/{productId}/delete")
    public ApiResponse<Void> deleteProduct(
            @CurrentManager final ManagerContext managerContext,
            @PathVariable("storeId") final long storeId,
            @PathVariable("productId") final long productId
    ) {
        deleteProductUseCase.command(
                new Command(
                        productId,
                        managerContext.getId(),
                        storeId
                )
        );
        return ApiResponse.ok();
    }
}
