package me.dgpr.domains.category.service;

import me.dgpr.domains.category.domain.Category;
import me.dgpr.domains.category.usecase.CreateCategoryUseCase;
import me.dgpr.persistence.entity.category.CategoryEntity;
import me.dgpr.persistence.service.category.CategoryCommand;
import org.springframework.stereotype.Service;

@Service
public class CreateCategory implements CreateCategoryUseCase {

    private final CategoryCommand categoryCommand;
    private final CategoryService categoryService;

    public CreateCategory(
            final CategoryCommand categoryCommand,
            final CategoryService categoryService
    ) {
        this.categoryCommand = categoryCommand;
        this.categoryService = categoryService;
    }

    @Override
    public Category command(final Command command) {

        categoryService.verifyManagerPermission(
                command.managerId(),
                command.storeId()
        );

        CategoryCommand.CreateCategory createCategory = new CategoryCommand.CreateCategory(
                command.storeId(),
                command.name()
        );

        CategoryEntity newCategory = categoryCommand.createNewCategory(createCategory);

        return new Category(
                newCategory.getId(),
                newCategory.getStoreId(),
                newCategory.getName()
        );
    }
}
