package me.dgpr.domains.store.service;

import me.dgpr.domains.store.domain.Store;
import me.dgpr.domains.store.usecase.CreateStoreUseCase;
import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.service.store.StoreCommand;
import org.springframework.stereotype.Service;

@Service
public class CreateStore implements CreateStoreUseCase {

    private final StoreCommand storeCommand;

    public CreateStore(final StoreCommand storeCommand) {
        this.storeCommand = storeCommand;
    }

    @Override
    public Store command(final Command command) {
        StoreCommand.CreateStore createStore = StoreCommand.CreateStore.of(
                command.managerId(),
                command.companyRegistrationNumber(),
                command.businessName()
        );

        StoreEntity newStore = storeCommand.createNewStore(createStore);

        return new Store(
                newStore.getId(),
                newStore.getManagerId(),
                newStore.getCompanyRegistrationNumber(),
                newStore.getBusinessName()
        );
    }
}
