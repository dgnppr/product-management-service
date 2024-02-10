package me.dgpr.api.category;

import me.dgpr.api.ApiResponse;
import me.dgpr.api.category.dto.CreateCategoryRequest;
import me.dgpr.config.security.CurrentManager;
import me.dgpr.config.security.ManagerContext;
import me.dgpr.domains.category.domain.Category;
import me.dgpr.domains.category.usecase.CreateCategoryUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryRestController(CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
    }

    @PostMapping("/v1/stores/{storeId}/categories")
    public ApiResponse<Category> createCategory(
            @CurrentManager ManagerContext managerContext,
            @PathVariable("storeId") final long storeId,
            @Validated @RequestBody final CreateCategoryRequest request
    ) {
        Category data = createCategoryUseCase.command(
                request.toCommand(
                        managerContext.getId(),
                        storeId
                )
        );
        return ApiResponse.created(data);
    }
}
