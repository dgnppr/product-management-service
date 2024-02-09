package me.dgpr.domains.manager.usecase;

import me.dgpr.domains.manager.domain.Manager;

public interface CreateManagerUseCase {

    Manager command(final Command command);
    
    record Command(
            String phoneNumber,
            String password
    ) {

    }
}
