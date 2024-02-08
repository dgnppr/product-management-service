package me.dgpr.persistence.service.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.repository.manager.ManagerRepository;
import me.dgpr.persistence.service.manager.ManagerCommand.CreateManager;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManagerCommandTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerCommand sut;

    @Test
    void 저장된_휴대폰_번호가_있으면_매니저_엔티티를_생성하지_않고_반환한다() {
        //Arrange
        String number = "01011112222";
        String password = "password";
        CreateManager createManager = new CreateManager(number, password);
        ManagerEntity manager = ManagerEntity.newManager(number, password);

        when(managerRepository.findByPhoneNumber(number))
                .thenReturn(Optional.of(manager));

        //Act
        ManagerEntity actual = sut.getOrCreateNewManager(createManager);

        //Assert
        assertThat(actual.getPhoneNumber()).isEqualTo(number);
        assertThat(actual.getPassword()).isEqualTo(password);
        verify(managerRepository, never()).save(any());
    }

    @Test
    void 저장되지_않은_휴대폰_번호로_새로운_매니저_엔티티를_생성할_수_있다() {
        //Arrange
        String number = "01011112222";
        String password = "password";
        CreateManager createManager = new CreateManager(number, password);

        when(managerRepository.findByPhoneNumber(number))
                .thenReturn(Optional.empty());

        when(managerRepository.save(any()))
                .then(returnsFirstArg());

        //Act
        ManagerEntity actual = sut.getOrCreateNewManager(createManager);

        //Assert
        assertThat(actual.getPhoneNumber()).isEqualTo(number);
        assertThat(actual.getPassword()).isEqualTo(password);
    }
}