package me.dgpr.domains.product.usecase;

import me.dgpr.domains.product.domain.ProductWithCategories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryProductUseCase {

    Page<ProductWithCategories> query(Query query);

    record Query(
            long storeId,
            Pageable pageable
    ) {

    }

}
