package me.dgpr.persistence.service.productcategory;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import me.dgpr.persistence.repository.productcategory.ProductCategoryRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductCategoryQueryTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductCategoryQuery sut;

    @Test
    void 상품_id를_이용해_상품_카테고리_리스트를_조회하여_반환한다() {
        //Arrange
        var productId = 1L;

        when(productCategoryRepository.findByProductId(eq(productId)))
                .thenReturn(
                        List.of(
                                ProductCategoryEntity.create(productId, 1L),
                                ProductCategoryEntity.create(productId, 2L),
                                ProductCategoryEntity.create(productId, 3L)
                        )
                );

        //Act
        var actual = sut.findByProductId(productId);

        //Assert
        assertThat(actual).hasSize(3)
                .extracting(ProductCategoryEntity::getProductId)
                .containsOnly(productId);
    }

    @Test
    void 존재하지_않는_카테고리_id로_엔티티_조회시_NotFoundCategoryException_예외_발생() {
        //Arrange

        //Act

        //Assert
    }
}