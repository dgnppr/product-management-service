package me.dgpr.persistence.service.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import me.dgpr.persistence.entity.store.StoreEntity;
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

    @InjectMocks
    private StoreCommand sut;

    @Test
    void 사업자_등록_번호외_상호명과_매니저_아이디로_새로운_Store_엔티티를_생성할_수_있다() {
        //Arrange
        var companyRegistrationNumber = "1234567890";
        var storeName = "storeName";
        var managerId = 1L;

        var command = new CreateStore(companyRegistrationNumber, storeName, managerId);

        when(storeRepository.save(any()))
                .then(returnsFirstArg());

        //Act
        StoreEntity actual = sut.createNewStore(command);

        //Assert
        assertThat(actual.getCompanyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
        assertThat(actual.getBusinessName()).isEqualTo(storeName);
        assertThat(actual.getManagerId()).isEqualTo(managerId);
    }
}