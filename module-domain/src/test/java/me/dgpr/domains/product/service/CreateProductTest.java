package me.dgpr.domains.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.config.exception.PermissionDeniedException;
import me.dgpr.domains.product.domain.Product;
import me.dgpr.domains.product.usecase.CreateProductUseCase;
import me.dgpr.domains.product.usecase.CreateProductUseCase.Command;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.service.product.ProductCommand;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand;
import me.dgpr.persistence.service.productname.ProductNameCommand;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateProductTest {

    @Mock
    private ProductService productService;
    @Mock
    private ProductCommand productCommand;
    @Mock
    private ProductCategoryCommand productCategoryCommand;
    @Mock
    private ProductNameCommand productNameCommand;
    @Mock
    private ProductNameSeparator productNameSeparator;
    @InjectMocks
    private CreateProduct sut;

    @Test
    void 가게_사장이_아닌_계정이_상품을_생성하면_PermissionDeniedException_예외_발생() {
        //Arrange
        var command = mock(Command.class);
        when(command.managerId()).thenReturn(1L);
        when(command.storeId()).thenReturn(1L);

        doThrow(PermissionDeniedException.class)
                .when(productService)
                .verifyManagerPermission(
                        anyLong(),
                        anyLong()
                );

        //Act & Assert
        assertThrows(
                PermissionDeniedException.class,
                () -> sut.command(command)
        );
    }

    @Test
    void 가게_id와_가격과_원가와_이름과_설명과_바코드와_유통기한과_사이즈로_새로운_Product를_생성한다() {
        //Arrange
        var managerId = 1L;
        var productId = 1L;
        var storeId = 1L;
        var cost = BigDecimal.valueOf(500);
        var price = BigDecimal.valueOf(1000);
        var name = "아이스 아메리카노";
        var description = "커피";
        var barcode = "123456789";
        var expirationDate = LocalDateTime.now();
        var size = "SMALL";
        var categoryIds = Set.of(1L, 2L);

        var mockProduct = mock(ProductEntity.class);
        when(mockProduct.getId()).thenReturn(productId);
        when(mockProduct.getStoreId()).thenReturn(storeId);
        when(mockProduct.getPrice()).thenReturn(Money.of(price));
        when(mockProduct.getCost()).thenReturn(Money.of(cost));
        when(mockProduct.getName()).thenReturn(name);
        when(mockProduct.getDescription()).thenReturn(description);
        when(mockProduct.getBarcode()).thenReturn(barcode);
        when(mockProduct.getExpirationDate()).thenReturn(expirationDate);
        when(mockProduct.getSize()).thenReturn(ProductSize.from(size));

        when(productCommand.createNewProduct(any()))
                .thenReturn(mockProduct);

        when(productNameSeparator.separate(eq(name)))
                .thenReturn(Set.of("아이스", "아메리카노", "ㅇㅇㅅ", "ㅇㅁㄹㅋㄴ"));

        var command = new CreateProductUseCase.Command(
                managerId,
                storeId,
                cost,
                price,
                name,
                description,
                barcode,
                expirationDate,
                size,
                categoryIds
        );

        //Act
        Product actual = sut.command(command);

        //Assert
        assertThat(actual.productId()).isEqualTo(productId);
        assertThat(actual.storeId()).isEqualTo(storeId);
        assertThat(actual.price()).isEqualTo(Money.of(price));
        assertThat(actual.cost()).isEqualTo(Money.of(cost));
        assertThat(actual.name()).isEqualTo(name);
        assertThat(actual.description()).isEqualTo(description);
        assertThat(actual.barcode()).isEqualTo(barcode);
        assertThat(actual.expirationDate()).isEqualTo(expirationDate);
        assertThat(actual.size()).isEqualTo(ProductSize.from(size));
    }

    @Test
    void 존재하지_않는_가게_id로_Product_생성_시_NotFoundException_예외_발생() {
        //Arrange
        when(productCommand.createNewProduct(any()))
                .thenThrow(NotFoundException.class);

        Command command = mock(Command.class);

        //Act & Assert
        assertThrows(
                NotFoundException.class,
                () -> sut.command(command)
        );
    }

    @Test
    void 존재하지_않는_카테고리_id로_Product_생성_시_NotFoundException_예외_발생() {
        //Arrange
        when(productCommand.createNewProduct(any()))
                .thenReturn(mock(ProductEntity.class));

        when(productCategoryCommand.createProductCategory(any()))
                .thenThrow(NotFoundException.class);

        Command command = mock(Command.class);

        //Act & Assert
        assertThrows(
                NotFoundException.class,
                () -> sut.command(command)
        );
    }
}