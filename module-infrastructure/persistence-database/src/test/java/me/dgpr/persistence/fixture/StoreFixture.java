package me.dgpr.persistence.fixture;

import me.dgpr.persistence.entity.store.StoreEntity;

public enum StoreFixture {

    DEFAULT_STORE(
            "123-45-67890",
            "default store"

    );

    private final String companyBusinessNumber;
    private final String businessName;

    StoreFixture(String companyBusinessNumber, String businessName) {
        this.companyBusinessNumber = companyBusinessNumber;
        this.businessName = businessName;
    }

    public StoreEntity create(final long managerId) {
        return StoreEntity.create(
                managerId,
                companyBusinessNumber,
                businessName
        );
    }
}
