package me.dgpr.persistence.repository.category.dto;

public record ProductCategoryDTO(
        long productId,
        long categoryId,
        String categoryName
) {

}
