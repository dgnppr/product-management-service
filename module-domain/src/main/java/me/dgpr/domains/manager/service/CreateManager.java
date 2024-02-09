package me.dgpr.domains.manager.service;

import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.exception.DuplicatedManagerException;
import me.dgpr.domains.manager.usecase.CreateManagerUseCase;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.service.manager.ManagerCommand;
import me.dgpr.persistence.service.manager.ManagerQuery;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateManager implements CreateManagerUseCase {

    private final ManagerCommand managerCommand;
    private final ManagerQuery managerQuery;
    private final PasswordEncoder passwordEncoder;

    public CreateManager(
            ManagerCommand managerCommand,
            ManagerQuery managerQuery,
            PasswordEncoder passwordEncoder
    ) {
        this.managerCommand = managerCommand;
        this.managerQuery = managerQuery;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Manager command(final Command command) {
        if (managerQuery.existsByPhoneNumber(command.phoneNumber())) {
            throw new DuplicatedManagerException(command.phoneNumber());
        }

        String encodedPassword = passwordEncoder.encode(command.password());

        ManagerEntity newManager = managerCommand.createNewManager(
                ManagerCommand.CreateManager.of(
                        command.phoneNumber(),
                        encodedPassword
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
