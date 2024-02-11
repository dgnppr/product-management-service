package me.dgpr.domains.product.service;

import java.util.List;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.domains.product.domain.ProductWithCategories;
import me.dgpr.domains.product.usecase.QueryProductsUseCase;
import me.dgpr.persistence.repository.product.dto.ProductWithCategoriesDTO;
import me.dgpr.persistence.service.product.ProductQuery;
import me.dgpr.persistence.service.store.StoreQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
public class QueryProducts implements QueryProductsUseCase {

    private final ProductQuery productQuery;
    private final StoreQuery storeQuery;

    public QueryProducts(
            ProductQuery productQuery,
            StoreQuery storeQuery
    ) {
        this.productQuery = productQuery;
        this.storeQuery = storeQuery;
    }

    @Override
    public Page<ProductWithCategories> query(final Query query) {
        if (!storeQuery.existsById(query.storeId())) {
            throw new NotFoundException(
                    "가게",
                    String.valueOf(query.storeId())
            );
        }

        Page<ProductWithCategoriesDTO> dtos = productQuery.findAllByStoreId(
                query.storeId(),
                query.pageable()
        );

        List<ProductWithCategories> productsWithCategories = dtos.getContent()
                .stream()
                .map(ProductWithCategories::from)
                .toList();

        return new PageImpl<>(
                productsWithCategories,
                dtos.getPageable(),
                dtos.getTotalElements()
        );
    }
}
