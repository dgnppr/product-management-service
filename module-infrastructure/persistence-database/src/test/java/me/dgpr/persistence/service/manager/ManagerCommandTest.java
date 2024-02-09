package me.dgpr.persistence.service.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void 저장되지_않은_휴대폰_번호로_새로운_매니저_엔티티를_생성할_수_있다() {
        //Arrange
        var number = "01011112222";
        var password = "password";
        var createManager = new CreateManager(number, password);
        
        when(managerRepository.save(any()))
                .then(returnsFirstArg());

        //Act
        ManagerEntity actual = sut.createNewManager(createManager);

        //Assert
        assertThat(actual.getPhoneNumber()).isEqualTo(number);
        assertThat(actual.getPassword()).isEqualTo(password);
    }
}