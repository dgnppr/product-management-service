package me.dgpr.persistence.service.store;

import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.store.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StoreCommand {

    private final StoreRepository storeRepository;

    public StoreCommand(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public StoreEntity createNewStore(final CreateStore command) {
        StoreEntity newStore = StoreEntity.create(
                command.managerId(),
                command.companyRegistrationNumber(),
                command.storeName()
        );

        return storeRepository.save(newStore);
    }

    public record CreateStore(
            String companyRegistrationNumber,
            String storeName,
            long managerId) {

    }
}
