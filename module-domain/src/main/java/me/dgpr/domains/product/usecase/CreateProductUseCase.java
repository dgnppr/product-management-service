package me.dgpr.domains.product.usecase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import me.dgpr.domains.product.domain.Product;

public interface CreateProductUseCase {

    Product command(Command command);

    record Command(
            long managerId,
            long storeId,
            BigDecimal price,
            BigDecimal cost,
            String name,
            String description,
            String barcode,
            LocalDateTime expirationDate,
            String size,
            Set<Long> categoryIds
    ) {

    }
}
