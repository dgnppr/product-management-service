package me.dgpr.domains.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import me.dgpr.domains.store.domain.Store;
import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.service.store.StoreCommand;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateStoreTest {

    @Mock
    private StoreCommand storeCommand;

    @InjectMocks
    private CreateStore sut;

    @Test
    void 사장님_id와_사업자_등록_번호와_상호명을_사용하여_StoreEntity를_생성할_수_있다() {
        //Arrange
        var managerId = 1L;
        var companyRegistrationNumber = "123-45-67890";
        var businessName = "상호명";
        var store = mock(StoreEntity.class);

        when(store.getId()).thenReturn(1L);
        when(store.getManagerId()).thenReturn(managerId);
        when(store.getCompanyRegistrationNumber()).thenReturn(companyRegistrationNumber);
        when(store.getBusinessName()).thenReturn(businessName);

        var command = new CreateStore.Command(
                managerId,
                companyRegistrationNumber,
                businessName
        );

        when(storeCommand.createNewStore(any()))
                .thenReturn(store);

        //Act
        Store actual = sut.command(command);

        //Assert
        assertThat(actual.managerId()).isEqualTo(managerId);
        assertThat(actual.companyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
        assertThat(actual.businessName()).isEqualTo(businessName);
    }
}