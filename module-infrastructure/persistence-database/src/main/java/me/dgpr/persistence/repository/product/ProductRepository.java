package me.dgpr.persistence.repository.product;

import me.dgpr.persistence.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // TODO 초성 검색

    /**
     * select * from product p where (p.deleted_at is NULL) and p.name like ? escape '\' offset ?
     * rows fetch first ? rows only
     */
    Page<ProductEntity> findByNameContaining(String name, Pageable pageable);
}
