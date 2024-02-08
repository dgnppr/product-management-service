package me.dgpr.persistence.repository.productname;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import me.dgpr.persistence.config.JpaConfiguration;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.entity.productname.ProductNameEntity;
import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.store.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = JpaConfiguration.class)
class ProductNameRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductNameRepository sut;

    @Test
    void 가게_id와_상품_이름을_사용하여_productEntity_page로_반환한다() {
        //[Arrange 1] StoreEntity 생성
        var store = storeRepository.save(StoreEntity.create(
                1L,
                "registration number",
                "store name")
        );

        //[Arrange 2] ProductEntities 생성
        var product1 = createProduct(store.getId(), "아이스 녹차 라떼");
        var product2 = createProduct(store.getId(), "아이스 카페 라떼");
        var product3 = createProduct(store.getId(), "아이스 초코 라떼");
        var product4 = createProduct(store.getId(), "핫초코");

        productRepository.saveAll(List.of(product1, product2, product3, product4));

        //[Arrange 3] ProductNameEntities 생성
        var productNames = List.of(
                ProductNameEntity.create(product1.getId(), "아이스"),
                ProductNameEntity.create(product1.getId(), "녹차"),
                ProductNameEntity.create(product1.getId(), "라떼"),
                ProductNameEntity.create(product2.getId(), "아이스"),
                ProductNameEntity.create(product2.getId(), "카페"),
                ProductNameEntity.create(product2.getId(), "라떼"),
                ProductNameEntity.create(product3.getId(), "아이스"),
                ProductNameEntity.create(product3.getId(), "초코"),
                ProductNameEntity.create(product3.getId(), "라떼"),
                ProductNameEntity.create(product4.getId(), "핫초코")
        );

        sut.saveAll(productNames);

        //[Arrange 4] 생성
        var pageNumber = 0;
        var pageSize = 2;
        var pageRequest = PageRequest.of(
                pageNumber,
                pageSize
        );

        var searchName = "아이스";

        //Act
        Page<ProductEntity> actual = sut.findProductsByStoreIdAndName(
                store.getId(), searchName, pageRequest);

        //Assert
        assertThat(actual.getTotalElements()).isEqualTo(3);
        assertThat(actual.getNumber()).isEqualTo(pageNumber);
        assertThat(actual.getSize()).isEqualTo(pageSize);
        assertThat(actual.getTotalPages()).isEqualTo(2);
    }

    @Test
    void 상품_id를_사용하여_상품_이름을_삭제하면_deleted_at이_활성화된다() {
        //Arrange
        var productId = 1L;
        var productNames = List.of(
                ProductNameEntity.create(productId, "아이스"),
                ProductNameEntity.create(productId, "녹차"),
                ProductNameEntity.create(productId, "라떼")
        );

        sut.saveAll(productNames);

        //Act
        sut.deleteAllByProductId(productId);

        //Assert
        assertThat(sut.findAll()).isEmpty();
    }

    public ProductEntity createProduct(
            long storeId,
            String productName
    ) {
        return ProductEntity.create(
                storeId,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(50),
                productName,
                "This is a test product",
                "123456789",
                LocalDateTime.now().plusDays(30),
                ProductSize.SMALL
        );
    }
}