package me.dgpr.persistence.repository.product;

import java.util.List;
import me.dgpr.persistence.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findAllByStoreId(long storeId, Pageable pageable);

    @Query("SELECT pe "
            + "FROM ProductEntity pe "
            + "JOIN ProductNameEntity pne on pe.id = pne.id "
            + "WHERE pe.storeId = :storeId AND (pe.name LIKE :name% OR pne.name LIKE :name%)")
    List<ProductEntity> findByStoreIdAndName(
            @Param("storeId") long storeId,
            @Param("name") String name
    );
}
