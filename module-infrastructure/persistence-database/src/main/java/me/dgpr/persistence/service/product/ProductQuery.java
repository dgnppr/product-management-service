package me.dgpr.persistence.service.product;

import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.service.product.exception.NotFoundProductException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductQuery {

    private final ProductRepository productRepository;

    public ProductQuery(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity findById(final long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException(String.valueOf(id)));
    }

    public Page<ProductEntity> findAllByStoreId(
            final long storeId,
            final Pageable pageable
    ) {
        return productRepository.findAllByStoreId(
                storeId,
                pageable
        );
    }

    public Page<ProductEntity> findByName(
            final String name,
            final Pageable pageable
    ) {
        return productRepository.findByNameContaining(name, pageable);
    }
}
