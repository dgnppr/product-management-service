package me.dgpr.domains.category.service;

import me.dgpr.common.exception.DuplicatedException;
import me.dgpr.common.exception.PermissionDeniedException;
import me.dgpr.persistence.service.category.CategoryQuery;
import me.dgpr.persistence.service.store.StoreQuery;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final StoreQuery storeQuery;
    private final CategoryQuery categoryQuery;

    public CategoryService(
            final StoreQuery storeQuery,
            final CategoryQuery categoryQuery
    ) {
        this.storeQuery = storeQuery;
        this.categoryQuery = categoryQuery;
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

    public void verifyCategoryNameIsUnique(
            final long storeId,
            final String name
    ) {
        if (categoryQuery.existsByStoreIdAndName(
                storeId,
                name)
        ) {
            throw new DuplicatedException(
                    "카테고리",
                    name
            );
        }

    }
}
