package me.dgpr.domains.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import me.dgpr.common.exception.NotFoundException;
import me.dgpr.config.exception.PermissionDeniedException;
import me.dgpr.domains.category.domain.Category;
import me.dgpr.domains.category.usecase.CreateCategoryUseCase.Command;
import me.dgpr.persistence.entity.category.CategoryEntity;
import me.dgpr.persistence.service.category.CategoryCommand;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateCategoryTest {

    @Mock
    private CategoryCommand categoryCommand;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CreateCategory sut;

    @Test
    void 가게_id와_카테고리_이름을_사용하여_새로운_Category를_생성할_수_있다() {
        //Arrange
        var managerId = 1L;
        var storeId = 1L;
        var categoryName = "test";

        var command = new Command(
                managerId,
                storeId,
                categoryName
        );

        var createCategory = new CategoryCommand.CreateCategory(
                storeId,
                categoryName
        );

        var mockCategory = mock(CategoryEntity.class);
        when(mockCategory.getStoreId()).thenReturn(storeId);
        when(mockCategory.getName()).thenReturn(categoryName);

        when(categoryCommand.createNewCategory(createCategory))
                .thenReturn(mockCategory);

        //Act
        Category actual = sut.command(command);

        //Assert
        assertThat(actual.storeId()).isEqualTo(storeId);
        assertThat(actual.name()).isEqualTo(categoryName);
    }

    @Test
    void 존재하지_않는_가게_id를_사용하여_카테고리를_생성할_시_NotFoundException_예외_발생() {
        //Arrange
        var managerId = 1L;
        var notExistStoreId = 1L;
        var categoryName = "test";

        var command = new Command(
                managerId,
                notExistStoreId,
                categoryName
        );

        var createCategory = new CategoryCommand.CreateCategory(
                notExistStoreId,
                categoryName
        );

        when(categoryCommand.createNewCategory(createCategory))
                .thenThrow(new NotFoundException(
                                "가게",
                                String.valueOf(notExistStoreId)
                        )
                );

        //Act & Assert
        assertThrows(
                NotFoundException.class,
                () -> sut.command(command)
        );
    }

    @Test
    void 사장이_아닌_계정이_가게_카테고리를_추가할_경우_PermissionDeniedException_예외_발생() {
        //Arrange
        var managerWhoNotStoreOwnerId = 1L;
        var storeId = 1L;
        var categoryName = "test";

        var command = new Command(
                managerWhoNotStoreOwnerId,
                storeId,
                categoryName
        );

        doThrow(new PermissionDeniedException("Manager does not have permission"))
                .when(categoryService)
                .verifyManagerPermission(
                        managerWhoNotStoreOwnerId,
                        storeId
                );

        //Act //Assert
        assertThrows(
                PermissionDeniedException.class,
                () -> sut.command(command)
        );
    }
}