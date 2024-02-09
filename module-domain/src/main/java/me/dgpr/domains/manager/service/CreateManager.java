package me.dgpr.domains.manager.service;

import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.exception.DuplicatedManagerException;
import me.dgpr.domains.manager.usecase.CreateManagerUseCase;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.service.manager.ManagerCommand;
import me.dgpr.persistence.service.manager.ManagerQuery;
import org.springframework.stereotype.Service;

@Service
public class CreateManager implements CreateManagerUseCase {

    private final ManagerCommand managerCommand;
    private final ManagerQuery managerQuery;

    public CreateManager(
            ManagerCommand managerCommand,
            ManagerQuery managerQuery
    ) {
        this.managerCommand = managerCommand;
        this.managerQuery = managerQuery;
    }

    @Override
    public Manager command(final Command command) {
        if (managerQuery.existsByPhoneNumber(command.phoneNumber())) {
            throw new DuplicatedManagerException(command.phoneNumber());
        }

        ManagerEntity newManager = managerCommand.createNewManager(
                ManagerCommand.CreateManager.of(
                        command.phoneNumber(),
                        command.password()
                )
        );

        return new Manager(
                newManager.getId(),
                newManager.getCreatedAt(),
                newManager.getLastModifiedAt(),
                newManager.getDeletedAt(),
                newManager.getPhoneNumber(),
                newManager.getPassword()
        );
    }
}
