package me.dgpr.domains.product.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import me.dgpr.config.exception.PermissionDeniedException;
import me.dgpr.domains.product.usecase.DeleteProductUseCase.Command;
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
class DeleteProductTest {

    @Mock
    private ProductCommand productCommand;
    @Mock
    private ProductCategoryCommand productCategoryCommand;
    @Mock
    private ProductNameCommand productNameCommand;
    @Mock
    private ProductService productService;
    @InjectMocks
    private DeleteProduct sut;

    @Test
    void 사장이_아닌_계정이_상품을_삭제하려고_하면_PermissionDeniedException_예외_발생() {
        //Arrange
        doThrow(PermissionDeniedException.class)
                .when(productService)
                .verifyManagerPermission(
                        anyLong(),
                        anyLong()
                );

        //Act & Assert
        assertThrows(
                PermissionDeniedException.class,
                () -> sut.command(mock(Command.class))
        );
    }

    @Test
    void 상품을_삭제하면_상품_카테고리와_상품_이름도_함께_삭제한다() {
        //Act
        sut.command(mock(Command.class));

        //Assert
        verify(productCategoryCommand).deleteAllByProductId(anyLong());
        verify(productNameCommand).deleteAllByProductId(anyLong());
        verify(productCommand).deleteById(anyLong());
    }
}