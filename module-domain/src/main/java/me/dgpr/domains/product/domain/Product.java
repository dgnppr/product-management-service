package me.dgpr.domains.product.domain;

import java.time.LocalDateTime;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;

public record Product(
        long productId,
        long storeId,
        Money price,
        Money cost,
        String name,
        String description,
        String barcode,
        LocalDateTime expirationDate,
        ProductSize size
) {

    public static Product from(ProductEntity product) {
        return new Product(
                product.getId(),
                product.getStoreId(),
                product.getPrice(),
                product.getCost(),
                product.getName(),
                product.getDescription(),
                product.getBarcode(),
                product.getExpirationDate(),
                product.getSize()
        );
    }
}
