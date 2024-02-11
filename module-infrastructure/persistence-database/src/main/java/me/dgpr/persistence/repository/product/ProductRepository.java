package me.dgpr.persistence.repository.product;

import me.dgpr.persistence.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findByNameContaining(String name, Pageable pageable);

    Page<ProductEntity> findAllByStoreId(long storeId, Pageable pageable);
}
