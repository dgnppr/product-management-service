package me.dgpr.domains.product.service;

import me.dgpr.domains.product.domain.ProductWithCategories;
import me.dgpr.domains.product.usecase.QueryProductByIdUseCase;
import me.dgpr.persistence.repository.product.dto.ProductWithCategoriesDTO;
import me.dgpr.persistence.service.product.ProductQuery;
import org.springframework.stereotype.Service;

@Service
public class QueryProductById implements QueryProductByIdUseCase {

    private final ProductQuery productQuery;

    public QueryProductById(ProductQuery productQuery) {
        this.productQuery = productQuery;
    }

    @Override
    public ProductWithCategories query(Query query) {
        ProductWithCategoriesDTO dto = productQuery.findByIdWithCategories(
                query.productId());

        return ProductWithCategories.from(dto);
    }
}
