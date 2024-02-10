package me.dgpr.api.category;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.dgpr.api.category.dto.CreateCategoryRequest;
import me.dgpr.api.support.AbstractMockMvcTest;
import me.dgpr.domains.category.domain.Category;
import me.dgpr.domains.category.usecase.CreateCategoryUseCase;
import me.dgpr.support.WithTestUser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CategoryRestControllerTest extends AbstractMockMvcTest {

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @CsvSource({"음료"})
    @ParameterizedTest(name = "올바른 카테고리 이름 입력: {0}")
    @WithTestUser
    void 카테고리_등록_성공(String categoryName) throws Exception {
        //Given
        var request = new CreateCategoryRequest(categoryName);
        var storeId = 1L;
        var categoryId = 1L;

        var category = new Category(
                categoryId,
                storeId,
                categoryName
        );

        when(createCategoryUseCase.command(any()))
                .thenReturn(category);

        //When & Then
        mockMvc.perform(
                        post("/v1/stores/{storeId}/categories", storeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code").value(HttpStatus.CREATED.value()));
    }

    @CsvSource({"' '"})
    @ParameterizedTest(name = "잘못된 카테고리 이름 입력: {0}")
    @WithTestUser
    void 카테고리_등록_실패(String categoryName) throws Exception {
        //Given
        var request = new CreateCategoryRequest(categoryName);

        //When & Then
        mockMvc.perform(
                        post("/v1/stores/{storeId}/categories", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))

                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.meta.code").value(HttpStatus.BAD_REQUEST.value()));
    }
}