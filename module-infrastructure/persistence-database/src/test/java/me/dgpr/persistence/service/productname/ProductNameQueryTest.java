package me.dgpr.persistence.service.productname;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.entity.productname.ProductNameEntity;
import me.dgpr.persistence.repository.productname.ProductNameRepository;
import me.dgpr.persistence.utils.Money;
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
class ProductNameQueryTest {

    @Mock
    private ProductNameRepository productNameRepository;

    @InjectMocks
    private ProductNameQuery sut;

    @Test
    void 가게_id와_상품_이름을_사용하여_ProductEntity를_페이지로_조회하여_반환한다() {
        // Arrange
        var storeId = 1L;
        var searchName = "아이스";

        var pageable = PageRequest.of(0, 10);
        var products = createProducts();
        var expectedPage = new PageImpl<>(products, pageable, products.size());

        when(productNameRepository.findProductsByStoreIdAndName(storeId, searchName, pageable))
                .thenReturn(expectedPage);

        // Act
        Page<ProductEntity> result = sut.findByStoreIdAndName(storeId, searchName, pageable);

        // Assert
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(expectedPage.getContent().size());
        assertThat(result.getTotalElements()).isEqualTo(products.size());
    }

    private List<ProductEntity> createProducts() {
        return List.of(
                ProductEntity.create(
                        1L,
                        Money.of(BigDecimal.valueOf(100)),
                        Money.of(BigDecimal.valueOf(50)),
                        "아이스 초코 라떼",
                        "초코라떼 입니다",
                        "123456788",
                        LocalDateTime.now().plusDays(30),
                        ProductSize.SMALL
                ),
                ProductEntity.create(
                        1L,
                        Money.of(BigDecimal.valueOf(100)),
                        Money.of(BigDecimal.valueOf(50)),
                        "아이스 카페 라떼",
                        "카페 라떼입니다.",
                        "123456789",
                        LocalDateTime.now().plusDays(30),
                        ProductSize.SMALL
                )
        );
    }

    private List<ProductNameEntity> createProductNames() {
        return List.of(
                ProductNameEntity.create(1L, "아이스"),
                ProductNameEntity.create(1L, "카페"),
                ProductNameEntity.create(1L, "라떼"),
                ProductNameEntity.create(2L, "아이스"),
                ProductNameEntity.create(2L, "초코"),
                ProductNameEntity.create(2L, "라떼")
        );
    }
}