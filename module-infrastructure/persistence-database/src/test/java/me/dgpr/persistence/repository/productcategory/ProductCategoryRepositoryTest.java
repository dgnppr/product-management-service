package me.dgpr.persistence.repository.productcategory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.dgpr.persistence.config.JpaConfiguration;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = JpaConfiguration.class)
class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository sut;

    @Test
    void 상품_id를_사용하여_상품_카테고리를_삭제하면_deletedAt이_활성화_된다() {
        //Arrange
        var productId = 1L;

        var productCategories = List.of(
                ProductCategoryEntity.create(productId, 1L),
                ProductCategoryEntity.create(productId, 2L),
                ProductCategoryEntity.create(productId, 3L),
                ProductCategoryEntity.create(productId, 4L)
        );

        sut.saveAll(productCategories);

        //Act
        sut.deleteAllByProductId(productId);

        //Assert
        assertThat(sut.findAll()).isEmpty();
    }
}