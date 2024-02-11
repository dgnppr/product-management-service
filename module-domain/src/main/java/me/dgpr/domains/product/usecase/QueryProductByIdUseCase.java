package me.dgpr.domains.product.usecase;

import me.dgpr.domains.product.domain.ProductWithCategories;

public interface QueryProductByIdUseCase {

    ProductWithCategories query(Query query);

    record Query(long productId) {

    }

}
