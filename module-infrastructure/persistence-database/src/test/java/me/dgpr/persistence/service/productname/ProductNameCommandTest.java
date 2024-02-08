package me.dgpr.persistence.service.productname;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.productname.ProductNameEntity;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.productname.ProductNameRepository;
import me.dgpr.persistence.service.product.exception.NotFoundProductException;
import me.dgpr.persistence.service.productname.ProductNameCommand.CreateCommand;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductNameCommandTest {

    @Mock
    private ProductNameRepository productNameRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductNameCommand sut;

    @Test
    void 존재하지_않는_상품_id로_ProductNameEntity를_생성하면_NotFoundProductException_예외_발생() {
        //Arrange
        var notExistingProductId = -1L;

        when(productRepository.findById(eq(notExistingProductId)))
                .thenReturn(Optional.empty());

        CreateCommand command = new CreateCommand(
                notExistingProductId,
                Set.of("아이스", "녹차", "라떼")
        );

        //Act //Assert
        assertThrows(NotFoundProductException.class, () -> sut.createProductNames(
                command
        ));
    }

    @Test
    void 상품_아이디와_상품_이름을_쪼개어서_매핑하여_새로운_ProductName_엔티티를_생성할_수_있다() {
        //Arrange
        var productId = 1L;
        var dividedProductName = Set.of("아이스", "녹차", "라떼");
        var command = new CreateCommand(
                productId,
                dividedProductName
        );

        when(productRepository.findById(eq(productId)))
                .thenReturn(Optional.of(mock(ProductEntity.class)));

        var productNames = dividedProductName.stream()
                .map(name ->
                        ProductNameEntity.create(
                                productId,
                                name
                        )
                ).toList();

        when(productNameRepository.saveAll(any()))
                .thenReturn(productNames);

        //Act
        int actual = sut.createProductNames(command);

        //Assert
        assertThat(actual).isEqualTo(dividedProductName.size());
    }

    @Test
    void 상품_id를_사용하여_ProductNameEntityList를_삭제할_수_있다() {
        //Arrange
        var productId = 1L;

        //Act
        sut.deleteAllByProductId(productId);

        //Assert
        verify(productNameRepository).deleteAllByProductId(productId);
    }
}