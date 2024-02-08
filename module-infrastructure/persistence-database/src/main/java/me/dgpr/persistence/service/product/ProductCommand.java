package me.dgpr.persistence.service.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.store.StoreRepository;
import me.dgpr.persistence.service.product.exception.NotFoundProductException;
import me.dgpr.persistence.service.store.exception.NotFoundStoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductCommand {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public ProductCommand(
            ProductRepository productRepository,
            StoreRepository storeRepository
    ) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    public ProductEntity createNewProduct(final CreateProduct command) {

        storeRepository.findById(command.storeId())
                .orElseThrow(() -> new NotFoundStoreException(String.valueOf(command.storeId())));

        ProductEntity productEntity = ProductEntity.create(
                command.storeId(),
                command.price(),
                command.cost(),
                command.name(),
                command.description(),
                command.barcode(),
                command.expirationDate(),
                command.size());

        return productRepository.save(productEntity);
    }

    public void updateProduct(
            final long id,
            final UpdateProduct command
    ) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException(String.valueOf(id)));

        productEntity.update(
                command.price(),
                command.cost(),
                command.name(),
                command.description(),
                command.barcode(),
                command.expirationDate(),
                command.size());

        productRepository.save(productEntity);
    }

    public void deleteById(final long id) {
        productRepository.deleteById(id);
    }

    public record CreateProduct(
            long storeId,
            BigDecimal price,
            BigDecimal cost,
            String name,
            String description,
            String barcode,
            LocalDateTime expirationDate,
            ProductSize size
    ) {

    }

    public record UpdateProduct(
            BigDecimal price,
            BigDecimal cost,
            String name,
            String description,
            String barcode,
            LocalDateTime expirationDate,
            ProductSize size
    ) {

    }
}
