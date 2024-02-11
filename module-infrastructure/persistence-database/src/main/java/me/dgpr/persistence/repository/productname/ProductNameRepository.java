package me.dgpr.persistence.repository.productname;

import java.util.List;
import java.util.Set;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.productname.ProductNameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductNameRepository extends JpaRepository<ProductNameEntity, Long> {

    @Query("SELECT pe "
            + "FROM ProductEntity pe "
            + "JOIN StoreEntity se ON se.id = pe.storeId  "
            + "JOIN ProductNameEntity pne ON pe.id = pne.productId "
            + "WHERE se.id = :storeId and pne.name = :name")
    Page<ProductEntity> findProductsByStoreIdAndName(
            @Param("storeId") long storeId,
            @Param("name") String name,
            Pageable pageable
    );

    @Transactional
    @Modifying
    @Query("UPDATE ProductNameEntity pne " +
            "SET pne.deletedAt = NOW() " +
            "WHERE pne.productId = :productId")
    void deleteAllByProductId(@Param("productId") long productId);

    @Transactional
    @Modifying
    @Query("UPDATE ProductNameEntity pne " +
            "SET pne.deletedAt = NOW() " +
            "WHERE pne.productId = :productId AND pne.name IN :names")
    void deleteByProductIdAndNames(
            @Param("productId") long productId,
            @Param("names") Set<String> names
    );

    List<ProductNameEntity> findByProductId(long productId);
}
