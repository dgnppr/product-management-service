package me.dgpr.persistence.repository.productcategory;

import java.util.List;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

    List<ProductCategoryEntity> findByProductId(long productId);
}
