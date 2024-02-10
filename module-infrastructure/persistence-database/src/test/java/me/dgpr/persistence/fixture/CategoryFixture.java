package me.dgpr.persistence.fixture;

import me.dgpr.persistence.entity.category.CategoryEntity;

public enum CategoryFixture {

    DEFAULT_CATEOGORY(
            "default category"
    );

    private final String name;

    CategoryFixture(final String name) {
        this.name = name;
    }

    public CategoryEntity create(final long storeId) {
        return CategoryEntity.create(
                storeId,
                name
        );
    }


}
