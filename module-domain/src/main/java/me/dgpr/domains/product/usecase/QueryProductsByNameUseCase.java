package me.dgpr.domains.product.usecase;

import java.util.List;
import me.dgpr.domains.product.domain.Product;

public interface QueryProductsByNameUseCase {

    List<Product> query(Query query);

    record Query(
            long storeId,
            String name
    ) {

    }
}
