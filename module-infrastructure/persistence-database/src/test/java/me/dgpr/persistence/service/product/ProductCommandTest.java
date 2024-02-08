package me.dgpr.persistence.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.store.StoreRepository;
import me.dgpr.persistence.service.product.ProductCommand.CreateProduct;
import me.dgpr.persistence.service.product.ProductCommand.UpdateProduct;
import me.dgpr.persistence.service.store.exception.NotFoundStoreException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductCommandTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private ProductCommand sut;

    @Test
    void 존재하지_않는_가게_아이디로_새로운_Product_엔티티를_만들경우_NotFoundStoreException_예외_발생() {
        //Arrange
        when(storeRepository.findById(any()))
                .thenReturn(Optional.empty());

        //Act //Assert
        assertThrows(
                NotFoundStoreException.class,
                () -> sut.createNewProduct(createCommand())
        );
    }

    @Test
    void 가게_아이디_가격_원가_이름_설명_바코드_유통기한_사이즈로_새로운_Product_엔티티를_생성할_수_있다() {
        //Arrange
        var command = createCommand();

        when(storeRepository.findById(any()))
                .thenReturn(Optional.of(mock(StoreEntity.class)));

        when(productRepository.save(any()))
                .then(returnsFirstArg());

        //Act
        ProductEntity actual = sut.createNewProduct(command);

        //Assert
        assertThat(actual.getPrice()).isEqualTo(command.price());
        assertThat(actual.getCost()).isEqualTo(command.cost());
        assertThat(actual.getName()).isEqualTo(command.name());
        assertThat(actual.getDescription()).isEqualTo(command.description());
        assertThat(actual.getBarcode()).isEqualTo(command.barcode());
        assertThat(actual.getExpirationDate()).isEqualTo(command.expirationDate());
        assertThat(actual.getSize()).isEqualTo(command.size());
    }

    @Test
    void 가격과_원가외_이름과_설명과_바코드외_유통기한괴_사이즈로_기존_Product_엔티티를_속성_일부를_수정할_수_있다() {
        //Arrange
        var productId = 1L;
        var product = createEntity();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        UpdateProduct command = updateCommand();

        when(productRepository.save(any()))
                .then(returnsFirstArg());

        //Act
        sut.updateProduct(productId, command);

        //Assert
        verify(productRepository).save(product);
    }

    @Test
    void id를_사용하여_Product_엔티티를_삭제할_수_있다() {
        //Arrange
        long productId = 1L;

        //Act
        sut.deleteById(productId);

        //Assert
        verify(productRepository).deleteById(productId);
    }

    private CreateProduct createCommand() {
        return new CreateProduct(
                1L,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(50),
                "Test Product",
                "This is a test product",
                "123456789",
                LocalDateTime.now().plusDays(30),
                ProductSize.SMALL);
    }

    private UpdateProduct updateCommand() {
        return new UpdateProduct(
                BigDecimal.valueOf(150),
                BigDecimal.valueOf(50),
                "Updated Product",
                "This is updated product",
                "987654321",
                LocalDateTime.now().plusDays(31),
                ProductSize.LARGE);
    }

    private ProductEntity createEntity() {
        return ProductEntity.create(
                1L,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(50),
                "Test Product",
                "This is a test product",
                "123456789",
                LocalDateTime.now().plusDays(30),
                ProductSize.SMALL);
    }
}