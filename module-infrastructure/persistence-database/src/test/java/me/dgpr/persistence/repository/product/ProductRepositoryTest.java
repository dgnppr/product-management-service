package me.dgpr.persistence.repository.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import me.dgpr.persistence.config.JpaConfiguration;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = JpaConfiguration.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository sut;

    // TODO 초성 검색

    @Test
    void 상품_이름을_like_검색으로_조회하여_Page로_반환한다() {
        //Arrange
        List<ProductEntity> products = List.of(
                createEntity("Test Product 1"),
                createEntity("Product Test 2"),
                createEntity("Product Test Test "),
                createEntity("Product"),
                createEntity("Test"),
                createEntity("Product 6")
        );

        sut.saveAll(products);

        int pageNumber = 0;
        int pageSize = 3;
        int expectedTotalElements = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        String findName = "Test";

        //Act
        Page<ProductEntity> actual = sut.findByNameContaining(findName, pageable);

        //Assert
        assertThat(actual.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(actual.getContent().size()).isEqualTo(pageSize);
        assertThat(actual.getNumber()).isEqualTo(pageNumber);
        assertThat(actual.getSize()).isEqualTo(pageSize);
    }

    @Test
    void 소프트_삭제를_하면_deletedAt_필드가_활성화_된다() {
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
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(50),
                productName,
                "This is a test product",
                "123456789",
                LocalDateTime.now().plusDays(30),
                ProductSize.SMALL);
    }
}