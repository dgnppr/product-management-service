package me.dgpr.persistence.service.productcategory;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import me.dgpr.persistence.repository.category.CategoryRepository;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.productcategory.ProductCategoryRepository;
import me.dgpr.persistence.service.category.exception.NotFoundCategoryException;
import me.dgpr.persistence.service.product.exception.NotFoundProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductCategoryCommand {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductCategoryCommand(
            ProductCategoryRepository productCategoryRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public int createProductCategory(final CreateProductCategory command) {
        // 1. ProductId로 ProductEntity 조회
        productRepository.findById(command.productId())
                .orElseThrow(
                        () -> new NotFoundProductException(String.valueOf(command.productId()))
                );

        // 2. CategoryIds로 CategoryEntities 조회
        Set<Long> categoryIds = command.categoryIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int categoryCounts = categoryRepository.countByIdIn(categoryIds);
        if (!Objects.equals(categoryCounts, command.categoryIds().size())) {
            throw new NotFoundCategoryException(String.valueOf(command.categoryIds()));
        }

        // 3. ProductCategoryEntity 생성
        List<ProductCategoryEntity> productCategories = command.categoryIds().stream()
                .map(it -> {
                    return ProductCategoryEntity.create(
                            command.productId,
                            it
                    );
                })
                .toList();

        return productCategoryRepository.saveAll(productCategories).size();
    }

    public record CreateProductCategory(
            long productId,
            Set<Long> categoryIds
    ) {

    }
}
