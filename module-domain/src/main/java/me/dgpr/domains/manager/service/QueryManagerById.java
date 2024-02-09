package me.dgpr.domains.manager.service;

import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.usecase.QueryManagerByIdUseCase;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.service.manager.ManagerQuery;
import org.springframework.stereotype.Service;

@Service
public class QueryManagerById implements QueryManagerByIdUseCase {

    private final ManagerQuery managerQuery;

    public QueryManagerById(ManagerQuery managerQuery) {
        this.managerQuery = managerQuery;
    }

    @Override
    public Manager query(Query query) {
        ManagerEntity managerEntity = managerQuery.findById(query.managerId());

        return new Manager(
                managerEntity.getId(),
                managerEntity.getCreatedAt(),
                managerEntity.getLastModifiedAt(),
                managerEntity.getDeletedAt(),
                managerEntity.getPhoneNumber(),
                managerEntity.getPassword()
        );
    }
}
