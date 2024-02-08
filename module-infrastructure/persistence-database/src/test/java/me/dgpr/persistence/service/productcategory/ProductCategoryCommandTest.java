package me.dgpr.persistence.service.productcategory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import me.dgpr.persistence.repository.category.CategoryRepository;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.productcategory.ProductCategoryRepository;
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
    void 제품_아이디와_카테고리_아이디_리스트로_새로운_ProductCategory_엔티티_리스트를_생성할_수_있다() {
        //Arrange
        var productId = 1L;
        var categoryIds = Set.of(1L, 2L, 3L);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(mock(ProductEntity.class)));

        when(categoryRepository.countByIdIn(categoryIds))
                .thenReturn(categoryIds.size());

        var command = new CreateProductCategory(
                productId,
                categoryIds
        );

        var productCategories = categoryIds.stream()
                .map(it -> {
                    return ProductCategoryEntity.create(
                            productId,
                            it
                    );
                })
                .toList();

        when(productCategoryRepository.saveAll(any()))
                .thenReturn(productCategories);

        //Act
        int actual = sut.createProductCategory(command);

        //Assert
        assertThat(actual).isEqualTo(categoryIds.size());
    }
}