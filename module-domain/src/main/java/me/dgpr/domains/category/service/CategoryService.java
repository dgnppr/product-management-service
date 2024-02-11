package me.dgpr.domains.category.service;

import me.dgpr.common.exception.PermissionDeniedException;
import me.dgpr.persistence.service.store.StoreQuery;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final StoreQuery storeQuery;

    public CategoryService(StoreQuery storeQuery) {
        this.storeQuery = storeQuery;
    }

    public void verifyManagerPermission(
            long managerId,
            long storeId
    ) {
        if (!storeQuery.existsByIdAndManagerId(
                storeId,
                managerId)
        ) {
            throw new PermissionDeniedException(
                    "Store",
                    String.format("storeId=%d, managerId=%d", storeId, managerId)
            );
        }
    }
}
