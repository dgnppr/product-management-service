package me.dgpr.domains.store.domain;

public record Store(
        long storeId,
        long managerId,
        String companyRegistrationNumber,
        String businessName
) {

    public static Store of(
            final long storeId,
            final long managerId,
            final String companyRegistrationNumber,
            final String businessName
    ) {
        return new Store(
                storeId,
                managerId,
                companyRegistrationNumber,
                businessName
        );
    }
}
