package me.dgpr.persistence.repository.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.config.JpaConfiguration;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = JpaConfiguration.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository sut;

    @Test
    void 삭제를_하면_deletedAt_필드가_활성화_된다() {
        //Arrange
        ProductEntity product = sut.save(createEntity("Test Product"));

        //Act
        sut.deleteById(product.getId());

        //Assert
        Optional<ProductEntity> actual = sut.findById(product.getId());
        assertThat(actual).isEmpty();
    }

    private ProductEntity createEntity(String productName) {
        return ProductEntity.create(
                1L,
                Money.of(BigDecimal.valueOf(100)),
                Money.of(BigDecimal.valueOf(50)),
                productName,
                "This is a test product",
                "123456789",
                LocalDateTime.now().plusDays(30),
                ProductSize.SMALL);
    }
}