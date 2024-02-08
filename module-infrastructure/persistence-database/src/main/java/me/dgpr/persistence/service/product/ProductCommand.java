package me.dgpr.persistence.service.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.service.product.exception.NotFoundProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductCommand {

    private final ProductRepository productRepository;

    public ProductCommand(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity createNewProduct(final CreateProduct command) {
        ProductEntity productEntity = ProductEntity.create(
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
            final UpdateProduct command) {
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
            BigDecimal price,
            BigDecimal cost,
            String name,
            String description,
            String barcode,
            LocalDateTime expirationDate,
            ProductSize size) {

    }

    public record UpdateProduct(
            BigDecimal price,
            BigDecimal cost,
            String name,
            String description,
            String barcode,
            LocalDateTime expirationDate,
            ProductSize size) {

    }
}
