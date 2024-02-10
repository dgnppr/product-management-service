package me.dgpr.persistence.service.productcategory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import me.dgpr.persistence.repository.category.CategoryRepository;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.productcategory.ProductCategoryRepository;
import me.dgpr.persistence.service.category.exception.NotFoundCategoryException;
import me.dgpr.persistence.service.product.exception.NotFoundProductException;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand.CreateProductCategory;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductCategoryCommandTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductCategoryCommand sut;

    @Test
    void 상품_id와_카테고리_id_set으로_새로운_ProductCategory_엔티티_리스트를_생성할_수_있다() {
        //Arrange
        var productId = 1L;
        var categoryIds = Set.of(1L, 2L, 3L);

        when(productRepository.existsById(eq(productId)))
                .thenReturn(true);

        when(categoryRepository.countByIdIn(categoryIds))
                .thenReturn(categoryIds.size());

        var command = new CreateProductCategory(
                productId,
                categoryIds
        );

        var productCategories = categoryIds.stream()
                .map(it -> ProductCategoryEntity.create(
                        productId,
                        it)
                )
                .toList();

        when(productCategoryRepository.saveAll(any()))
                .thenReturn(productCategories);

        //Act
        int actual = sut.createProductCategory(command);

        //Assert
        assertThat(actual).isEqualTo(categoryIds.size());
    }

    @Test
    void 존재하지_않는_상품_id로_ProductCategory_엔티티를_생성할_시_NotFoundProductException_예외_발생() {
        //Arrange
        var notExistingProductId = -1L;
        when(productRepository.existsById(eq(notExistingProductId)))
                .thenReturn(false);

        var command = new CreateProductCategory(
                notExistingProductId,
                Set.of(1L, 2L, 3L)
        );

        //Act //Assert
        assertThrows(
                NotFoundProductException.class,
                () -> sut.createProductCategory(command)
        );
    }

    @Test
    void 존재하지_않는_카테고리_id_set로_ProductCategory_엔티티를_생성할_시_NotFoundCategoryException_예외_발생() {
        //Arrange
        when(productRepository.existsById(any()))
                .thenReturn(true);

        var notExistingCategoryIds = Set.of(1L, 2L, 3L);

        when(categoryRepository.countByIdIn(notExistingCategoryIds))
                .thenReturn(0);

        var command = new CreateProductCategory(
                1L,
                notExistingCategoryIds
        );

        //Act //Assert
        assertThrows(
                NotFoundCategoryException.class,
                () -> sut.createProductCategory(command)
        );
    }

    @Test
    void 상품_id를_사용하여_상품_카테고리를_삭제할_수_있다() {
        //Arrange
        var productId = 1L;

        //Act
        sut.deleteAllByProductId(productId);

        //Assert
        verify(productCategoryRepository).deleteAllByProductId(productId);
    }
}