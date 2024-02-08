package me.dgpr.persistence.entity.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import me.dgpr.persistence.config.BaseEntity;

@Entity
@Table(name = "store")
public class StoreEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    private String companyRegistrationNumber;

    private String businessName;

    private long managerId;

    protected StoreEntity() {
    }

    private StoreEntity(
            final String companyRegistrationNumber,
            final String businessName,
            final Long managerId
    ) {
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.businessName = businessName;
        this.managerId = managerId;
    }

    public static StoreEntity create(
            final String companyRegistrationNumber,
            final String storeName,
            final Long managerId
    ) {
        return new StoreEntity(companyRegistrationNumber, storeName, managerId);
    }

    public Long getId() {
        return id;
    }

    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public Long getManagerId() {
        return managerId;
    }
}
