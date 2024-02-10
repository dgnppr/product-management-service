package me.dgpr.domains.manager.usecase;

public interface QueryLogoutByIdUseCase {

    boolean query(final Query query);

    record Query(
            long managerId,
            String token
    ) {

    }
}
