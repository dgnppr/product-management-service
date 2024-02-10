package me.dgpr.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import me.dgpr.api.auth.dto.LoginRequest;
import me.dgpr.api.auth.dto.LoginResponse;
import me.dgpr.config.security.JwtTokenHandler;
import me.dgpr.config.security.JwtTokenProperties;
import me.dgpr.domains.manager.exception.InvalidPasswordException;
import me.dgpr.domains.manager.usecase.LoginManagerUseCase;
import me.dgpr.domains.manager.usecase.LogoutManagerUseCase;
import me.dgpr.fixture.manager.ManagerFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthServiceTest {

    @Mock
    private LoginManagerUseCase loginManagerUseCase;

    @Mock
    private LogoutManagerUseCase logoutManagerUseCase;

    @Mock
    private JwtTokenHandler jwtTokenHandler;

    @Mock
    private JwtTokenProperties jwtTokenProperties;

    @InjectMocks
    private AuthService sut;

    @Test
    void 휴대폰_번호와_password를_사용하여_LoginResponse를_반환한다() {
        //Arrange
        var manager = ManagerFixture.DEFAULT_MANAGER.instance();
        var phoneNumber = manager.phoneNumber();
        var password = manager.password();
        var token = "token";

        var request = new LoginRequest(
                phoneNumber,
                password
        );

        when(loginManagerUseCase.query(request.toQuery()))
                .thenReturn(manager);

        when(jwtTokenProperties.getSecretKey())
                .thenReturn("secretKey");

        when(jwtTokenProperties.getExpirationTime())
                .thenReturn(1000L);

        when(jwtTokenHandler.generateToken(
                any(),
                any(),
                any(),
                anyLong()
        )).thenReturn(token);

        //Act
        LoginResponse actual = sut.login(request);

        //Assert
        assertThat(actual).isNotNull();
        assertThat(actual.token()).isEqualTo(token);
    }

    @Test
    void 잘못된_비밀번호로_로그인할_시_InvalidPasswordException_예외_발생() {
        //Arrange
        var phoneNumber = "01012345678";
        var password = "password";

        var query = new LoginManagerUseCase.Query(phoneNumber, password);

        when(loginManagerUseCase.query(eq(query)))
                .thenThrow(new InvalidPasswordException("managerId"));

        var request = new LoginRequest(phoneNumber, password);

        //Act & Assert
        assertThrows(
                InvalidPasswordException.class,
                () -> sut.login(request)
        );
    }

    @Test
    void 토큰에서_id를_추출하여_로그아웃_토큰을_저장한다() {
        //Arrange
        var token = "token";
        var secretKey = "secretKey";
        var managerId = 1L;

        when(jwtTokenProperties.getExpirationTime())
                .thenReturn(1000L);

        when(jwtTokenProperties.getSecretKey())
                .thenReturn(secretKey);

        when(jwtTokenHandler.verifyAndGetIdFromToken(
                eq(secretKey),
                eq(token)
        )).thenReturn(managerId);

        //Act
        sut.logout(token);

        //Assert
        verify(logoutManagerUseCase).command(any());
    }
}