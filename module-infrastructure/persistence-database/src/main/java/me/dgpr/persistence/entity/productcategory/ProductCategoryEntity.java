package me.dgpr.persistence.entity.productcategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_category")
public class ProductCategoryEntity {

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
