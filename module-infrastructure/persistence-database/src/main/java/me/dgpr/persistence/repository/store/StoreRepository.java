package me.dgpr.persistence.repository.store;

import me.dgpr.persistence.entity.store.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    boolean existsByIdAndManagerId(
            long storeId,
            long managerId
    );
}
