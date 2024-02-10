package me.dgpr.domains.manager.usecase;

public interface QueryLogoutByTokenUseCase {

    boolean query(final Query query);

    record Query(
            long managerId,
            String token
    ) {

    }
}
