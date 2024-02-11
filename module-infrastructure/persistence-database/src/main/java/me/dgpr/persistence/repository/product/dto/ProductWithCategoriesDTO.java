package me.dgpr.persistence.repository.product.dto;

import java.time.LocalDateTime;
import java.util.Set;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductSize;

public record ProductWithCategoriesDTO(
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

}
