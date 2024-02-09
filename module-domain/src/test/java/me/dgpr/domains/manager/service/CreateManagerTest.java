package me.dgpr.domains.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.exception.DuplicatedManagerException;
import me.dgpr.domains.manager.usecase.CreateManagerUseCase.Command;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.service.manager.ManagerCommand;
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
class CreateManagerTest {

    @Mock
    private ManagerCommand managerCommand;

    @Mock
    private ManagerQuery managerQuery;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateManager sut;

    @Test
    void 휴대폰_번호와_비밀번호로_새로운_Manager를_생성할_수_있다() {
        //Arrange
        var phoneNumber = "01011112222";
        var password = "password";

        var createManager = ManagerCommand.CreateManager.of(
                phoneNumber,
                password
        );

        var managerEntity = ManagerEntity.create(
                phoneNumber,
                password
        );

        when(managerQuery.existsByPhoneNumber(eq(phoneNumber)))
                .thenReturn(false);

        when(managerCommand.createNewManager(eq(createManager)))
                .thenReturn(managerEntity);

        when(passwordEncoder.encode(eq(password)))
                .thenReturn(password);

        var command = new Command(phoneNumber, password);

        //Act
        Manager actual = sut.command(command);

        //Assert
        assertThat(actual.phoneNumber()).isEqualTo(phoneNumber);
        assertThat(actual.password()).isEqualTo(password);
    }

    @Test
    void 이미_저장된_휴대폰_번호로_Manager를_생성하려고_하면_DuplicatedManagerException_예외_발생() {
        //Arrange

        var phoneNumber = "01011112222";
        var password = "password";

        when(managerQuery.existsByPhoneNumber(eq(phoneNumber)))
                .thenReturn(true);

        //Act //Assert
        assertThrows(
                DuplicatedManagerException.class,
                () -> sut.command(new Command(phoneNumber, password))
        );
    }
}