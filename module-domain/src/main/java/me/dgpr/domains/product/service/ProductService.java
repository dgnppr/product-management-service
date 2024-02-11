package me.dgpr.domains.product.service;

import me.dgpr.common.exception.PermissionDeniedException;
import me.dgpr.persistence.service.store.StoreQuery;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final StoreQuery storeQuery;

    public ProductService(StoreQuery storeQuery) {
        this.storeQuery = storeQuery;
    }

    public void verifyManagerPermission(
            final long managerId,
            final long storeId
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
