package me.dgpr.persistence.service.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.store.StoreRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StoreQueryTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreQuery sut;

    @Test
    void id를_이용해_엔티티_조회_성공_시_엔티티_응답한다() {
        //Arrange
        var companyRegistrationNumber = "1234567890";
        var storeName = "storeName";
        var managerId = 1L;
        var storeEntity = StoreEntity.create(
                managerId,
                companyRegistrationNumber,
                storeName
        );

        var storeId = 1L;

        when(storeRepository.findById(storeId))
                .thenReturn(Optional.of(storeEntity));

        //Act
        StoreEntity actual = sut.findById(storeId);

        //then
        assertThat(actual.getCompanyRegistrationNumber()).isEqualTo(
                storeEntity.getCompanyRegistrationNumber());
        assertThat(actual.getBusinessName()).isEqualTo(storeEntity.getBusinessName());
        assertThat(actual.getManagerId()).isEqualTo(storeEntity.getManagerId());
    }

    @Test
    void id를_이용해_엔티티_조회_실패_시_NotFoundException_예외가_발생한다() {
        //Arrange
        long notExistingStoreId = 1L;

        when(storeRepository.findById(notExistingStoreId))
                .thenReturn(Optional.empty());

        //Act //Assert
        assertThrows(
                NotFoundException.class,
                () -> sut.findById(notExistingStoreId)
        );
    }
}