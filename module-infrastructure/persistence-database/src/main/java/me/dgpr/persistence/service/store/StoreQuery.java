package me.dgpr.persistence.service.store;

import me.dgpr.persistence.entity.store.StoreEntity;
import me.dgpr.persistence.repository.store.StoreRepository;
import me.dgpr.persistence.service.store.exception.NotFoundStoreException;
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
                .orElseThrow(() -> new NotFoundStoreException(String.valueOf(storeId)));
    }
}
