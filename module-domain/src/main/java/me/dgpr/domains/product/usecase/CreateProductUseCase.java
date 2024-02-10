package me.dgpr.domains.product.usecase;

import java.time.LocalDateTime;
import java.util.Set;
import me.dgpr.domains.product.domain.Product;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductSize;

public interface CreateProductUseCase {

    Product command(Command command);

    record Command(
            long storeId,
            Money price,
            Money cost,
            String name,
            String description,
            String barcode,
            LocalDateTime expirationDate,
            ProductSize size,
            Set<Long> categoryIds
    ) {

    }
}
