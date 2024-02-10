package me.dgpr.api.store;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.dgpr.api.store.dto.CreateStoreRequest;
import me.dgpr.api.support.AbstractMockMvcTest;
import me.dgpr.domains.store.domain.Store;
import me.dgpr.domains.store.usecase.CreateStoreUseCase;
import me.dgpr.support.WithTestUser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class StoreRestControllerTest extends AbstractMockMvcTest {

    @MockBean
    private CreateStoreUseCase createStoreUseCase;

    @CsvSource({"1234-567-890, storeName"})
    @ParameterizedTest(name = "올바른 사업자 등록 번호: {1}, 올바른 상호명: {2}")
    @WithTestUser
    void 사장님_가게_등록_성공(
            final String companyRegistrationNumber,
            final String businessName
    ) throws Exception {
        //Given
        final long managerId = 1L;
        var request = new CreateStoreRequest(
                companyRegistrationNumber,
                businessName
        );

        var storeId = 1;
        var store = Store.of(
                storeId,
                managerId,
                companyRegistrationNumber,
                businessName
        );

        when(createStoreUseCase.command(eq(request.toCommand(managerId))))
                .thenReturn(store);

        //When & Then
        mockMvc.perform(
                        post("/v1/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code").value(HttpStatus.CREATED.value()));
    }

    @CsvSource({" , "})
    @ParameterizedTest(name = "잘못된 사업자 등록 번호: {1}, 잘못된 상호명: {2}")
    @WithTestUser()
    void 사장님_가게_등록_실패(
            final String companyRegistrationNumber,
            final String businessName
    ) throws Exception {
        //Given
        var request = new CreateStoreRequest(
                companyRegistrationNumber,
                businessName
        );

        //When & Then
        mockMvc.perform(
                        post("/v1/stores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.meta.code").value(HttpStatus.BAD_REQUEST.value()));
    }
}