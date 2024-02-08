package me.dgpr.persistence.service.productcategory;

import java.util.List;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import me.dgpr.persistence.repository.productcategory.ProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductCategoryQuery {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryQuery(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategoryEntity> findByProductId(final long productId) {
        return productCategoryRepository.findByProductId(productId);
    }
}
