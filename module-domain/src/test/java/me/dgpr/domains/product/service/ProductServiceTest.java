package me.dgpr.domains.product.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import me.dgpr.config.exception.PermissionDeniedException;
import me.dgpr.persistence.service.store.StoreQuery;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductServiceTest {

    @Mock
    private StoreQuery storeQuery;
    @InjectMocks
    private ProductService productService;

    @Test
    void 가게_사장이_아니면_PermissionDeniedException_예외_발생() {
        //Arrange
        when(storeQuery.existsByIdAndManagerId(
                anyLong(),
                anyLong()
        )).thenReturn(false);

        //Act & Assert
        assertThrows(
                PermissionDeniedException.class,
                () -> productService.verifyManagerPermission(
                        anyLong(),
                        anyLong()
                )
        );
    }
}