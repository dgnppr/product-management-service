package me.dgpr.api.manager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.dgpr.api.manager.dto.CreateManagerRequest;
import me.dgpr.api.support.AbstractMockMvcTest;
import me.dgpr.domains.manager.usecase.CreateManagerUseCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ManagerRestControllerTest extends AbstractMockMvcTest {

    @MockBean
    private CreateManagerUseCase createManagerUseCase;

    @CsvSource({"01012345678,thisispassword"})
    @ParameterizedTest(name = "올바른 휴대폰 번호 입력 : {0}, 올바른 비밀번호 입력: {1}")
    void 사장님_회원가입_성공(String validPhoneNumber, String validPassword) throws Exception {
        //given
        var request = new CreateManagerRequest(
                validPhoneNumber,
                validPassword
        );

        //when & then
        mockMvc.perform(
                        post("/v1/managers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code").value(HttpStatus.CREATED.value()));
    }

    @CsvSource({
            ", ",
            "010-1234-5678, passwor",
            "010-1234-56789, passwordpasswordpassw"
    })
    @ParameterizedTest(name = "잘못된 휴대폰 번호 입력 : {0}, 잘못된 비밀번호 입력: {1}")
    void 사장님_회원가입_실패(String invalidPhoneNumber, String invalidPassword) throws Exception {
        //Given
        var request = new CreateManagerRequest(
                invalidPhoneNumber,
                invalidPassword
        );

        //When & Then
        mockMvc.perform(
                        post("/v1/managers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request))
                )
                .andExpect(status().isBadRequest());
    }
}