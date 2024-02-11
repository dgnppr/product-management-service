package me.dgpr.domains.product.domain;

import java.time.LocalDateTime;
import java.util.Set;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.repository.product.dto.ProductWithCategoriesDTO;

public record ProductWithCategories(
        long productId,
        long storeId,
        Money price,
        Money cost,
        String name,
        String description,
        String barcode,
        LocalDateTime expirationDate,
        ProductSize size,
        Set<String> categoryNames
) {

    public static ProductWithCategories from(final ProductWithCategoriesDTO dto) {
        return new ProductWithCategories(
                dto.productId(),
                dto.storeId(),
                dto.price(),
                dto.cost(),
                dto.name(),
                dto.description(),
                dto.barcode(),
                dto.expirationDate(),
                dto.size(),
                dto.categoryNames()
        );
    }

}
