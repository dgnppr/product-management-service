package me.dgpr.domains.manager.service;

import me.dgpr.common.exception.AuthenticationException;
import me.dgpr.domains.manager.domain.Manager;
import me.dgpr.domains.manager.usecase.LoginManagerUseCase;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.service.manager.ManagerQuery;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginManager implements LoginManagerUseCase {

    private final ManagerQuery managerQuery;
    private final PasswordEncoder passwordEncoder;

    public LoginManager(final ManagerQuery managerQuery,
            PasswordEncoder passwordEncoder
    ) {
        this.managerQuery = managerQuery;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Manager query(final Query query) {
        ManagerEntity managerEntity = managerQuery.findByPhoneNumber(query.phoneNumber());

        if (!passwordEncoder.matches(query.password(), managerEntity.getPassword())) {
            throw new AuthenticationException();
        }

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
