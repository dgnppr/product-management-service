package me.dgpr.persistence.service.store;

import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.manager.ManagerRepository;
import me.dgpr.persistence.repository.store.StoreRepository;
import me.dgpr.persistence.service.manager.exception.NotFoundManagerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StoreCommand {

    private final StoreRepository storeRepository;
    private final ManagerRepository managerRepository;

    public StoreCommand(
            final StoreRepository storeRepository,
            final ManagerRepository managerRepository
    ) {
        this.storeRepository = storeRepository;
        this.managerRepository = managerRepository;
    }

    public StoreEntity createNewStore(final CreateStore command) {
        if (!managerRepository.existsById(command.managerId())) {
            throw new NotFoundManagerException(String.valueOf(command.managerId()));
        }
        
        StoreEntity newStore = StoreEntity.create(
                command.managerId(),
                command.companyRegistrationNumber(),
                command.storeName()
        );

        return storeRepository.save(newStore);
    }

    public record CreateStore(
            long managerId,
            String companyRegistrationNumber,
            String storeName
    ) {

        public static CreateStore of(
                long managerId,
                String companyRegistrationNumber,
                String storeName
        ) {
            return new CreateStore(
                    managerId,
                    companyRegistrationNumber,
                    storeName
            );
        }

    }
}
