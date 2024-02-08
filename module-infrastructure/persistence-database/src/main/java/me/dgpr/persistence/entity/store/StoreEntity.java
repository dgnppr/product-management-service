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
    private long managerId;
    private String companyRegistrationNumber;
    private String businessName;

    protected StoreEntity() {
    }

    private StoreEntity(
            final Long managerId,
            final String companyRegistrationNumber,
            final String businessName
    ) {
        this.managerId = managerId;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.businessName = businessName;
    }

    public static StoreEntity create(
            final Long managerId,
            final String companyRegistrationNumber,
            final String storeName
    ) {
        return new StoreEntity(
                managerId,
                companyRegistrationNumber,
                storeName
        );
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
