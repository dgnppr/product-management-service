package me.dgpr.domains.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.usecase.QueryManagerByIdUseCase;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.service.manager.ManagerQuery;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QueryManagerByIdTest {

    @Mock
    private ManagerQuery managerQuery;

    @InjectMocks
    private QueryManagerById sut;

    @Test
    void 사장님_id를_사용하여_manager를_반환한다() {
        //Arrange
        var managerId = 1L;
        var mockManager = mock(ManagerEntity.class);

        when(mockManager.getId())
                .thenReturn(managerId);

        when(managerQuery.findById(managerId))
                .thenReturn(mockManager);

        var query = new QueryManagerByIdUseCase.Query(managerId);

        //Act
        Manager actual = sut.query(query);

        //Assert
        assertThat(actual.id()).isEqualTo(managerId);
    }

}