package me.dgpr.domains.product.service;

import java.util.List;
import me.dgpr.domains.product.domain.Product;
import me.dgpr.domains.product.usecase.QueryProductsByNameUseCase;
import me.dgpr.persistence.service.product.ProductQuery;
import org.springframework.stereotype.Service;

@Service
public class QueryProductsByName implements QueryProductsByNameUseCase {

    private final ProductQuery productQuery;

    public QueryProductsByName(ProductQuery productQuery) {
        this.productQuery = productQuery;
    }

    @Override
    public List<Product> query(final Query query) {
        return productQuery.findByStoreIdAndName(
                        query.storeId(),
                        query.name()
                )
                .stream()
                .map(Product::from)
                .toList();
    }
}
