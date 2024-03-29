package me.dgpr.persistence.entity.product;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.config.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deleted_at = NOW() WHERE product_id = ?")
@Where(clause = "deleted_at is NULL")
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private long storeId;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "cost_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "cost_currency"))
    })
    private Money cost;
    private String name;
    private String description;
    private String barcode;
    private LocalDateTime expirationDate;
    @Enumerated(EnumType.STRING)
    private ProductSize size;

    protected ProductEntity() {
    }

    private ProductEntity(
            final long storeId,
            final Money price,
            final Money cost,
            final String name,
            final String description,
            final String barcode,
            final LocalDateTime expirationDate,
            final ProductSize size
    ) {
        this.storeId = storeId;
        this.price = price;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.expirationDate = expirationDate;
        this.size = size;
    }

    public static ProductEntity create(
            final long storeId,
            final Money price,
            final Money cost,
            final String name,
            final String description,
            final String barcode,
            final LocalDateTime expirationDate,
            final ProductSize size
    ) {
        return new ProductEntity(
                storeId,
                price,
                cost,
                name,
                description,
                barcode,
                expirationDate,
                size
        );
    }

    public void update(
            Money price,
            Money cost,
            String name,
            String description,
            String barcode,
            LocalDateTime expirationDate,
            ProductSize size
    ) {
        this.price = price;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.expirationDate = expirationDate;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public long getStoreId() {
        return storeId;
    }

    public Money getPrice() {
        return price;
    }

    public Money getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBarcode() {
        return barcode;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public ProductSize getSize() {
        return size;
    }
}
