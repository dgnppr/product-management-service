package me.dgpr.domains.manager.usecase;

import me.dgpr.domains.manager.domain.Manager;

public interface LoginManagerUseCase {

    Manager query(final Query query);

    record Query(
            String phoneNumber,
            String password
    ) {

    }
}
