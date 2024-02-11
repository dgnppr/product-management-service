package me.dgpr.persistence.service.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.manager.ManagerRepository;
import me.dgpr.persistence.repository.store.StoreRepository;
import me.dgpr.persistence.service.store.StoreCommand.CreateStore;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StoreCommandTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private StoreCommand sut;

    @Test
    void 사업자_등록_번호외_상호명과_사장님_id로_새로운_Store_엔티티를_생성할_수_있다() {
        //Arrange
        var companyRegistrationNumber = "1234567890";
        var storeName = "storeName";
        var managerId = 1L;

        var command = new CreateStore(
                managerId,
                companyRegistrationNumber,
                storeName
        );

        when(storeRepository.save(any()))
                .then(returnsFirstArg());

        when(managerRepository.existsById(managerId))
                .thenReturn(true);

        //Act
        StoreEntity actual = sut.createNewStore(command);

        //Assert
        assertThat(actual.getCompanyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
        assertThat(actual.getBusinessName()).isEqualTo(storeName);
        assertThat(actual.getManagerId()).isEqualTo(managerId);
    }

    @Test
    void 존재하지_않는_사장님_id로_엔티티를_생성할_시_NotFoundException_예외_발생() {
        //Arrange
        var notExistingManagerId = -1L;

        when(managerRepository.existsById(notExistingManagerId))
                .thenReturn(false);

        var command = new CreateStore(
                notExistingManagerId,
                "1234567890",
                "storeName"
        );

        //Act * Assert
        assertThrows(
                NotFoundException.class,
                () -> sut.createNewStore(command)
        );
    }
}