package me.dgpr.domains.store.usecase;

import me.dgpr.domains.store.domain.Store;

public interface CreateStoreUseCase {

    Store command(final Command command);

    record Command(
            long managerId,
            String companyRegistrationNumber,
            String businessName
    ) {

    }

}
