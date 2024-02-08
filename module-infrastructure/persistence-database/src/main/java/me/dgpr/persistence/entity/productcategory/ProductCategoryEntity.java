package me.dgpr.persistence.entity.productcategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import me.dgpr.persistence.config.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "product_category")
@SQLDelete(sql = "UPDATE product_category SET deleted_at = NOW() WHERE product_category_id = ?")
@Where(clause = "deleted_at is NULL")
public class ProductCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    private Long id;
    private long productId;
    private long categoryId;

    protected ProductCategoryEntity() {
    }

    private ProductCategoryEntity(
            final long productId,
            final long categoryId
    ) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    public static ProductCategoryEntity create(
            final long productId,
            final long categoryId
    ) {
        return new ProductCategoryEntity(productId, categoryId);
    }

    public Long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public long getCategoryId() {
        return categoryId;
    }
}
