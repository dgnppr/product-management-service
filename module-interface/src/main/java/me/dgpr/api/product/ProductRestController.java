package me.dgpr.api.product;

import static org.springframework.data.domain.Sort.Direction.DESC;

import me.dgpr.api.ApiResponse;
import me.dgpr.api.product.dto.CreateProductRequest;
import me.dgpr.api.product.dto.UpdateProductRequest;
import me.dgpr.config.security.CurrentManager;
import me.dgpr.config.security.ManagerContext;
import me.dgpr.domains.product.domain.ProductWithCategories;
import me.dgpr.domains.product.service.QueryProducts;
import me.dgpr.domains.product.usecase.CreateProductUseCase;
import me.dgpr.domains.product.usecase.DeleteProductUseCase;
import me.dgpr.domains.product.usecase.DeleteProductUseCase.Command;
import me.dgpr.domains.product.usecase.QueryProductUseCase;
import me.dgpr.domains.product.usecase.QueryProductsUseCase;
import me.dgpr.domains.product.usecase.UpdateProductUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final QueryProductsUseCase queryProductsUseCase;
    private final QueryProductUseCase queryProductUseCase;

    public ProductRestController(
            CreateProductUseCase createProductUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase,
            QueryProductsUseCase queryProductsUseCase,
            QueryProductUseCase queryProductUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.queryProductsUseCase = queryProductsUseCase;
        this.queryProductUseCase = queryProductUseCase;
    }

    @GetMapping("/v1/stores/{storeId}/products")
    public ApiResponse<Page<ProductWithCategories>> getProducts(
            @PathVariable("storeId") final long storeId,
            @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        Page<ProductWithCategories> data = queryProductsUseCase.query(
                new QueryProducts.Query(
                        storeId,
                        pageable
                )
        );
        return ApiResponse.ok(data);
    }

    @GetMapping("/v1/stores/{storeId}/products/{productId}")
    public ApiResponse<ProductWithCategories> getProduct(
            @PathVariable("storeId") final long storeId,
            @PathVariable("productId") final long productId
    ) {
        ProductWithCategories data = queryProductUseCase.query(
                new QueryProductUseCase.Query(
                        productId
                )
        );
        return ApiResponse.ok(data);
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
