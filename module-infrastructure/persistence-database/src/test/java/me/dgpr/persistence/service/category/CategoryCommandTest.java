package me.dgpr.persistence.service.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import me.dgpr.persistence.entity.category.CategoryEntity;
import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.category.CategoryRepository;
import me.dgpr.persistence.repository.store.StoreRepository;
import me.dgpr.persistence.service.category.CategoryCommand.CreateCategory;
import me.dgpr.persistence.service.store.exception.NotFoundStoreException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CategoryCommandTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private CategoryCommand sut;

    @Test
    void 존재하지_않는_가게_아이디로_Cateogry_엔티티_생성_시_NotFoundCategoryException_예외_발생한다() {
        //Arrange
        var notExistingStoreId = -1L;

        when(storeRepository.findById(eq(notExistingStoreId)))
                .thenReturn(Optional.empty());

        CreateCategory command = new CreateCategory(
                notExistingStoreId,
                "name"
        );

        //Act //Assert
        assertThrows(
                NotFoundStoreException.class,
                () -> sut.createNewCategory(command)
        );
    }

    @Test
    void 가게_id_카테고리_이름으로_새로운_Category_엔티티를_생성할_수_있다() {
        //Arrange
        var storeId = 1L;
        var categoryName = "name";

        when(storeRepository.findById(eq(storeId)))
                .thenReturn(Optional.of(mock(StoreEntity.class)));

        var expected = CategoryEntity.create(
                storeId,
                categoryName
        );

        when(categoryRepository.save(any()))
                .thenReturn(expected);

        var command = new CreateCategory(
                storeId,
                categoryName
        );

        //Act
        CategoryEntity actual = sut.createNewCategory(command);

        //Assert
        assertThat(actual.getStoreId()).isEqualTo(expected.getStoreId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }
}