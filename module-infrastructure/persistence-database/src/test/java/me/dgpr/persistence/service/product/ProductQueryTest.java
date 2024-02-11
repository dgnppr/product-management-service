package me.dgpr.persistence.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductQueryTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductQuery sut;

    @Test
    void id를_이용해_엔티티_조회_성공_시_엔티티_응답한다() {
        //Arrange
        var productId = 1L;
        var product = ProductEntity.create(
                1L,
                Money.of(BigDecimal.valueOf(100)),
                Money.of(BigDecimal.valueOf(50)),
                "Test Product",
                "This is a test product",
                "123456789",
                LocalDateTime.now().plusDays(30),
                ProductSize.SMALL);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        //Act
        ProductEntity actual = sut.findById(productId);

        //Assert
        assertThat(actual.getPrice()).isEqualTo(product.getPrice());
        assertThat(actual.getCost()).isEqualTo(product.getCost());
        assertThat(actual.getName()).isEqualTo(product.getName());
        assertThat(actual.getDescription()).isEqualTo(product.getDescription());
        assertThat(actual.getBarcode()).isEqualTo(product.getBarcode());
        assertThat(actual.getExpirationDate()).isEqualTo(product.getExpirationDate());
        assertThat(actual.getSize()).isEqualTo(product.getSize());
    }

    @Test
    void id를_이용해_엔티티_조회_실패_시_NotFoundException_예외가_발생한다() {
        //Arrange
        var notExistingProductId = 1L;
        when(productRepository.findById(notExistingProductId))
                .thenReturn(Optional.empty());

        //Act //Assert
        assertThrows(
                NotFoundException.class,
                () -> sut.findById(notExistingProductId)
        );
    }

    @Test
    void 가게_id를_사용하여_ProductEntity_페이지로_응답한다() {
        //Arrange
        var totalElements = 100;
        var pageNumber = 0;
        var pageSize = 10;

        var storeId = 1L;
        var products = createProducts(totalElements);
        var pageable = PageRequest.of(pageNumber, pageSize);
        var expected = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAllByStoreId(storeId, pageable))
                .thenReturn(expected);

        //Act
        Page<ProductEntity> actual = sut.findAllByStoreId(
                storeId,
                pageable
        );

        //Assert
        assertThat(actual.getTotalElements()).isEqualTo(expected.getTotalElements());
        assertThat(actual.getContent()).isEqualTo(expected.getContent());
        assertThat(actual.getNumber()).isEqualTo(pageNumber);
        assertThat(actual.getSize()).isEqualTo(pageSize);
    }

    @Test
    void 상품_이름으로_ProducEntity_페이지를_응답한다() {
        // Arrange
        var totalElements = 5;
        var pageNumber = 0;
        var pageSize = 2;

        var searchName = "Test Product";

        var products = createProducts(totalElements);
        var pageable = PageRequest.of(pageNumber, pageSize);
        var expected = new PageImpl<>(products, pageable, totalElements);

        when(productRepository.findByNameContaining(searchName, pageable))
                .thenReturn(expected);

        // Act
        Page<ProductEntity> actual = sut.findByName(searchName, pageable);

        // Assert
        assertThat(actual.getTotalElements()).isEqualTo(expected.getTotalElements());
        assertThat(actual.getContent().size()).isEqualTo(expected.getContent().size());
        assertThat(actual.getNumber()).isEqualTo(pageNumber);
        assertThat(actual.getSize()).isEqualTo(pageSize);
    }

    private List<ProductEntity> createProducts(int size) {
        List<ProductEntity> products = new ArrayList<>();
        IntStream.range(0, size).forEach(i -> {
            ProductEntity product = ProductEntity.create(
                    1L,
                    Money.of(BigDecimal.valueOf(100)),
                    Money.of(BigDecimal.valueOf(50)),
                    "Test Product " + i,
                    "This is a test product",
                    "123456789",
                    LocalDateTime.now().plusDays(30),
                    ProductSize.SMALL);
            products.add(product);
        });

        return products;
    }
}