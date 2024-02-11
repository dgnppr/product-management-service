package me.dgpr.persistence.repository.productcategory;

import java.util.List;
import java.util.Set;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import me.dgpr.persistence.repository.category.dto.ProductCategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

    List<ProductCategoryEntity> findByProductId(long productId);

    @Transactional
    @Modifying
    @Query("UPDATE ProductCategoryEntity pce " +
            "SET pce.deletedAt = NOW() " +
            "WHERE pce.productId = :productId"
    )
    void deleteAllByProductId(@Param("productId") long productId);

    @Transactional
    @Modifying
    @Query("UPDATE ProductCategoryEntity pce " +
            "SET pce.deletedAt = NOW() " +
            "WHERE pce.productId = :productId AND pce.categoryId IN :categoryIds"
    )
    int deleteAllByProductIdAndCategoryIdIn(
            @Param("productId") long productId,
            @Param("categoryIds") Set<Long> categoryIds
    );

    @Query("SELECT new me.dgpr.persistence.repository.category.dto.ProductCategoryDTO(pce.productId, pce.categoryId, ce.name) "
            +
            "FROM ProductCategoryEntity pce " +
            "JOIN CategoryEntity ce ON pce.categoryId = ce.id " +
            "WHERE pce.productId IN :productIds"
    )
    List<ProductCategoryDTO> findByProductIdIn(Set<Long> productIds);
}
