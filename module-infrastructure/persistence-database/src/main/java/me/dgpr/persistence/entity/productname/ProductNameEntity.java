package me.dgpr.persistence.entity.productname;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import me.dgpr.persistence.config.BaseEntity;

@Entity
@Table(name = "product_name")
public class ProductNameEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_name_id")
    private Long id;
    private Long productId;
    private String name;

    protected ProductNameEntity() {
    }

    private ProductNameEntity(
            final Long productId,
            final String name
    ) {
        this.productId = productId;
        this.name = name;
    }

    public static ProductNameEntity create(
            final Long productId,
            final String name
    ) {
        return new ProductNameEntity(
                productId,
                name
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }
}
