package me.dgpr.persistence.service.store;

import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.store.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StoreQuery {

    private final StoreRepository storeRepository;

    public StoreQuery(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public StoreEntity findById(final long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(
                                "가게",
                                String.valueOf(storeId)
                        )
                );
    }

    public boolean existsByIdAndManagerId(
            final long storeId,
            final long managerId
    ) {
        return storeRepository.existsByIdAndManagerId(
                storeId,
                managerId
        );
    }

    public boolean existsById(final long storeId) {
        return storeRepository.existsById(storeId);
    }
}
