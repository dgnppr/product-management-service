package me.dgpr.domains.category.domain;

public record Category(
        Long categoryId,
        long storeId,
        String name
) {

}
