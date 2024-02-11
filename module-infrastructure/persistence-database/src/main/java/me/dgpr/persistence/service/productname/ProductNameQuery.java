package me.dgpr.persistence.service.productname;

import java.util.List;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.productname.ProductNameEntity;
import me.dgpr.persistence.repository.productname.ProductNameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductNameQuery {

    private final ProductNameRepository productNameRepository;

    public ProductNameQuery(ProductNameRepository productNameRepository) {
        this.productNameRepository = productNameRepository;
    }

    public Page<ProductEntity> findByStoreIdAndName(
            final long storeId,
            final String name,
            final Pageable pageable
    ) {
        return productNameRepository.findProductsByStoreIdAndName(
                storeId,
                name,
                pageable
        );
    }

    public List<ProductNameEntity> findByProductId(final long productId) {
        return productNameRepository.findByProductId(productId);
    }
}
