package me.dgpr.domains.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import me.dgpr.common.exception.AuthenticationException;
import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.service.manager.ManagerQuery;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LoginManagerTest {

    @Mock
    private ManagerQuery managerQuery;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginManager sut;

    @Test
    void 올바른_비밀번호로_로그인_성공하면_manager_반환() {
        //Arrange
        var phoneNumber = "01012345678";
        var rawPassword = "rawPassword";
        var encodedPassword = "encodedPassword";

        var mockManager = mock(ManagerEntity.class);

        when(mockManager.getPhoneNumber())
                .thenReturn(phoneNumber);

        when(mockManager.getPassword())
                .thenReturn(encodedPassword);

        when(managerQuery.findByPhoneNumber(eq(phoneNumber)))
                .thenReturn(mockManager);

        when(passwordEncoder.matches(eq(rawPassword), eq(encodedPassword)))
                .thenReturn(true);

        var query = new LoginManager.Query(
                phoneNumber,
                rawPassword
        );

        //Act
        Manager actual = sut.query(query);

        //Assert
        assertThat(actual.phoneNumber()).isEqualTo(phoneNumber);
        assertThat(actual.password()).isEqualTo(encodedPassword);
    }

    @Test
    void 잘못된_비밀번호로_로그인하면_InvalidPasswordException_예외_발생() {
        //Arrange
        var phoneNumber = "01012345678";
        var rawPassword = "rawPassword";
        var encodedPassword = "encodedPassword";

        var mockManager = mock(ManagerEntity.class);

        when(mockManager.getPassword())
                .thenReturn(encodedPassword);

        when(managerQuery.findByPhoneNumber(eq(phoneNumber)))
                .thenReturn(mockManager);

        when(passwordEncoder.matches(eq(rawPassword), eq(encodedPassword)))
                .thenReturn(false);

        var query = new LoginManager.Query(
                phoneNumber,
                rawPassword
        );

        //Act & Assert
        assertThrows(
                AuthenticationException.class,
                () -> sut.query(query)
        );
    }
}