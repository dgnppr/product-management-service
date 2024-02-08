package me.dgpr.persistence.entity.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import me.dgpr.persistence.config.BaseEntity;

@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private long storeId;
    private String name;

    protected CategoryEntity() {
    }

    private CategoryEntity(
            final long storeId,
            final String name
    ) {
        this.storeId = storeId;
        this.name = name;
    }

    public static CategoryEntity create(
            final long storeId,
            final String name
    ) {
        return new CategoryEntity(storeId, name);
    }

    public Long getId() {
        return id;
    }

    public long getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }
}
