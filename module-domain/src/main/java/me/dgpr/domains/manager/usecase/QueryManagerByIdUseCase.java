package me.dgpr.domains.manager.usecase;

import me.dgpr.domains.manager.domain.Manager;

public interface QueryManagerByIdUseCase {

    Manager query(final Query query);

    record Query(
            long managerId
    ) {

    }
}
