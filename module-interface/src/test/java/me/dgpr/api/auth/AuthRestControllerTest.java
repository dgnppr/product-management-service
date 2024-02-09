package me.dgpr.api.auth;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.dgpr.api.auth.dto.LoginRequest;
import me.dgpr.api.auth.dto.LoginResponse;
import me.dgpr.api.auth.service.AuthService;
import me.dgpr.api.support.AbstractMockMvcTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AuthRestControllerTest extends AbstractMockMvcTest {

    @MockBean
    private AuthService authService;

    @CsvSource({"01012345678,thisispassword"})
    @ParameterizedTest(name = "올바른 휴대폰 번호 입력 : {0}, 올바른 비밀번호 입력: {1}")
    void 사장님_로그인_성공(String validPhoneNumber, String validPassword) throws Exception {
        //given
        var request = new LoginRequest(
                validPhoneNumber,
                validPassword
        );

        var token = "token";
        var response = new LoginResponse(token);

        when(authService.login(request))
                .thenReturn(response);

        //when & then
        mockMvc.perform(
                        post("/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value(token));
    }

    @CsvSource({
            ", ",
            "010-1234-5678, passwor",
            "010-1234-56789, passwordpasswordpassw"
    })
    @ParameterizedTest(name = "잘못된 휴대폰 번호 입력 : {0}, 잘못된 비밀번호 입력: {1}")
    void 사장님_로그인_실패(String invalidPhoneNumber, String invalidPassword) throws Exception {
        //given
        var request = new LoginRequest(
                invalidPhoneNumber,
                invalidPassword
        );

        //when & then
        mockMvc.perform(
                        post("/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.meta.code").value(HttpStatus.BAD_REQUEST.value()));
    }
}