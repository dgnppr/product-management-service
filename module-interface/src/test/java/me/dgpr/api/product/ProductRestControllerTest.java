package me.dgpr.api.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import me.dgpr.api.product.dto.CreateProductRequest;
import me.dgpr.api.support.AbstractMockMvcTest;
import me.dgpr.domains.product.usecase.CreateProductUseCase;
import me.dgpr.support.WithTestUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ProductRestControllerTest extends AbstractMockMvcTest {

    @MockBean
    private CreateProductUseCase createProductUseCase;

    @Test
    @WithTestUser
    void 상품_등록_성공() throws Exception {
        //Given
        var price = BigDecimal.valueOf(1000);
        var cost = BigDecimal.valueOf(500);
        var name = "콜라";
        var description = "콜라";
        var barcode = "1234567890";
        var expirationDate = LocalDateTime.now();
        var size = "SMALL";
        var categoryIds = Set.of(1L, 2L);

        var request = new CreateProductRequest(
                price,
                cost,
                name,
                description,
                barcode,
                expirationDate,
                size,
                categoryIds
        );

        //When & Then
        mockMvc.perform(
                        post("/v1/stores/{storeId}/products", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code").value(HttpStatus.CREATED.value()));
    }

    @Test
    @WithTestUser
    void 상품_등록_실패() throws Exception {
        //Given
        var price = BigDecimal.valueOf(1000);
        var cost = BigDecimal.valueOf(500);
        var name = " ";
        var description = " ";
        var barcode = " ";
        var expirationDate = LocalDateTime.now();
        var size = "SMALL";
        var categoryIds = Set.of(1L, 2L);

        var request = new CreateProductRequest(
                price,
                cost,
                name,
                description,
                barcode,
                expirationDate,
                size,
                categoryIds
        );

        //When & Then
        mockMvc.perform(
                        post("/v1/stores/{storeId}/products", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.meta.code").value(HttpStatus.BAD_REQUEST.value()));
    }
}