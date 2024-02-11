package me.dgpr.domains.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.common.exception.PermissionDeniedException;
import me.dgpr.domains.product.domain.Product;
import me.dgpr.domains.product.usecase.UpdateProductUseCase;
import me.dgpr.domains.product.usecase.UpdateProductUseCase.Command;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.service.category.CategoryQuery;
import me.dgpr.persistence.service.product.ProductCommand;
import me.dgpr.persistence.service.product.ProductQuery;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand;
import me.dgpr.persistence.service.productcategory.ProductCategoryQuery;
import me.dgpr.persistence.service.productname.ProductNameCommand;
import me.dgpr.persistence.service.productname.ProductNameQuery;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UpdateProductTest {

    @Mock
    private ProductService productService;
    @Mock
    private CategoryQuery categoryQuery;
    @Mock
    private ProductCommand productCommand;
    @Mock
    private ProductQuery productQuery;
    @Mock
    private ProductNameSeparator productNameSeparator;
    @Mock
    private ProductNameCommand productNameCommand;
    @Mock
    private ProductNameQuery productNameQuery;
    @Mock
    private ProductCategoryQuery productCategoryQuery;
    @Mock
    private ProductCategoryCommand productCategoryCommand;
    @InjectMocks
    private UpdateProduct sut;

    @Test
    void 사장님이_아닌_계정으로_상품을_업데이트하려고_하면_PermissionDeniedException_예외_발생() {
        //Arrange
        doThrow(PermissionDeniedException.class)
                .when(productService)
                .verifyManagerPermission(
                        anyLong(),
                        anyLong()
                );

        var command = mock(Command.class);

        //Act & Assert
        assertThrows(
                PermissionDeniedException.class,
                () -> sut.command(command)
        );
    }

    @Test
    void 존재하지_않는_카테고리_id로_상품을_업데이트하려고_하면_NotFoundException_예외_발생() {
        //Arrange
        var command = mock(Command.class);
        var notExistingCategoryIds = Set.of(1L, 2L);

        when(command.categoryIds())
                .thenReturn(notExistingCategoryIds);

        doThrow(NotFoundException.class)
                .when(categoryQuery)
                .existsByIds(eq(notExistingCategoryIds));

        //Act & Assert
        assertThrows(
                NotFoundException.class,
                () -> sut.command(command)
        );
    }

    @Test
    void 가게_id와_가격과_원가와_이름과_설명과_바코드와_유통기한과_사이즈로_상품과_상품_카테고리와_상품_이름을_업데이트할_수_있다() {
        //Arrange
        var command = new UpdateProductUseCase.Command(
                1L,
                1L,
                1L,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(500),
                "아이스 아메리카노",
                "커피",
                "123456789",
                LocalDateTime.now(),
                "SMALL",
                Set.of(1L, 2L)
        );

        var productEntity = mock(ProductEntity.class);
        when(productEntity.getId()).thenReturn(1L);
        when(productEntity.getStoreId()).thenReturn(1L);
        when(productEntity.getPrice()).thenReturn(Money.of(BigDecimal.valueOf(1000)));
        when(productEntity.getCost()).thenReturn(Money.of(BigDecimal.valueOf(500)));
        when(productEntity.getName()).thenReturn("아이스 아메리카노");
        when(productEntity.getDescription()).thenReturn("커피");
        when(productEntity.getBarcode()).thenReturn("123456789");
        when(productEntity.getExpirationDate()).thenReturn(LocalDateTime.now());
        when(productEntity.getSize()).thenReturn(ProductSize.from("SMALL"));

        when(productQuery.findById(anyLong()))
                .thenReturn(productEntity);

        //Act
        Product product = sut.command(command);

        //Assert
        assertThat(product).isNotNull();
    }
}