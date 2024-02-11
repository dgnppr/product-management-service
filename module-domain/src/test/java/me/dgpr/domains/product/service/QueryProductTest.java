package me.dgpr.domains.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.domains.product.domain.ProductWithCategories;
import me.dgpr.domains.product.usecase.QueryProductsByStoreIdUseCase.Query;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.repository.product.dto.ProductWithCategoriesDTO;
import me.dgpr.persistence.service.product.ProductQuery;
import me.dgpr.persistence.service.store.StoreQuery;
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
class QueryProductTest {

    @Mock
    private ProductQuery productQuery;
    @Mock
    private StoreQuery storeQuery;
    @InjectMocks
    private QueryProductsByStoreId sut;

    @Test
    void 존재하지_않는_가게_id로_쿼리시_NotFoundException_예외_발생() {
        //Arrange
        when(storeQuery.existsById(anyLong()))
                .thenReturn(false);

        //Act & Assert
        assertThrows(
                NotFoundException.class,
                () -> sut.query(mock(QueryProductsByStoreId.Query.class)
                )
        );
    }

    @Test
    void 가게_id와_pageable를_사용하여_ProductWithCategories_페이지네이션_반환() {
        // Arrange
        var storeId = 1L;
        var pageable = PageRequest.of(
                0,
                10
        );
        var query = new Query(storeId, pageable);

        when(storeQuery.existsById(storeId))
                .thenReturn(true);

        var dtos = List.of(
                new ProductWithCategoriesDTO(
                        1L,
                        storeId,
                        Money.of(BigDecimal.valueOf(1000)),
                        Money.of(BigDecimal.valueOf(500)),
                        "Product Name",
                        "Description",
                        "Barcode",
                        LocalDateTime.now(),
                        ProductSize.SMALL,
                        Set.of("Category1", "Category2"))
        );
        var expected = new PageImpl<>(dtos, pageable, dtos.size());

        when(productQuery.findAllByStoreId(eq(storeId), eq(pageable)))
                .thenReturn(expected);

        // Act
        Page<ProductWithCategories> actual = sut.query(query);

        // Assert
        assertThat(actual.getTotalElements()).isEqualTo(expected.getTotalElements());
        assertThat(actual.getTotalPages()).isEqualTo(expected.getTotalPages());
        assertThat(actual.getContent().size()).isEqualTo(expected.getContent().size());
    }
}