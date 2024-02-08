package me.dgpr.persistence.service.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.repository.manager.ManagerRepository;
import me.dgpr.persistence.service.manager.exception.NotFoundManagerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManagerQueryTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerQuery sut;

    @Test
    void id를_이용해_엔티티_조회_성공_시_엔티티_응답() {
        //Arrange
        var managerId = 1L;
        var phoneNumber = "01011112222";
        var password = "password";
        var manager = ManagerEntity.create(phoneNumber, password);

        when(managerRepository.findById(managerId))
                .thenReturn(Optional.of(manager));

        //Act
        ManagerEntity actual = sut.findById(managerId);

        //then
        assertThat(actual.getPhoneNumber()).isEqualTo(manager.getPhoneNumber());
        assertThat(actual.getPassword()).isEqualTo(manager.getPassword());
    }

    @Test
    void 휴대폰_번호를_이용해_엔티티_조회_성공_시_엔티티_응답() {
        //Arrange
        var phoneNumber = "01011112222";
        var password = "password";
        var manager = ManagerEntity.create(phoneNumber, password);

        when(managerRepository.findByPhoneNumber(phoneNumber))
                .thenReturn(Optional.of(manager));

        //Act
        ManagerEntity actual = sut.findByPhoneNumber(phoneNumber);

        //then
        assertThat(actual.getPhoneNumber()).isEqualTo(manager.getPhoneNumber());
        assertThat(actual.getPassword()).isEqualTo(manager.getPassword());
    }

    @Test
    void id를_이용해_엔티티_조회_실패_시_NotFoundManagerException_예외_발생한다() {
        //Arrange
        var notExistingManagerId = -1L;
        when(managerRepository.findById(notExistingManagerId))
                .thenReturn(Optional.empty());

        //Act //Assert
        assertThrows(
                NotFoundManagerException.class,
                () -> sut.findById(notExistingManagerId)
        );
    }

    @Test
    void 휴대폰_번호를_이용해_엔티티_조회_실패_시_NotFoundManagerException_예외_발생한다() {
        //Arrange
        var notExistingPhoneNumber = "01011112222";
        when(managerRepository.findByPhoneNumber(notExistingPhoneNumber))
                .thenReturn(Optional.empty());

        //Act //Assert
        assertThrows(
                NotFoundManagerException.class,
                () -> sut.findByPhoneNumber(notExistingPhoneNumber)
        );
    }
}