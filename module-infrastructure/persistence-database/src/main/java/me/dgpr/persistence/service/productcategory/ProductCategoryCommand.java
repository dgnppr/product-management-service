package me.dgpr.persistence.service.productcategory;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import me.dgpr.persistence.repository.category.CategoryRepository;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.productcategory.ProductCategoryRepository;
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
        // 1. 상품 ID로 상품 조회
        if (!productRepository.existsById(command.productId())) {
            throw new NotFoundException(
                    "상품",
                    String.valueOf(command.productId()));
        }

        // 2. 카테고리 ID Set으로 카테고리 조회
        Set<Long> categoryIds = command.categoryIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int categoryCounts = categoryRepository.countByIdIn(categoryIds);
        if (!Objects.equals(categoryCounts, command.categoryIds().size())) {
            throw new NotFoundException(
                    "카테고리",
                    String.valueOf(command.categoryIds())
            );
        }

        // 3. 상품 카테고리 리스트 생성
        List<ProductCategoryEntity> productCategories = command.categoryIds().stream()
                .map(it ->
                        ProductCategoryEntity.create(
                                command.productId,
                                it
                        )
                ).toList();

        // 4. 상품 카테고리 리스트 저장
        return productCategoryRepository.saveAll(productCategories).size();
    }

    public int deleteProductCategory(final DeleteProductCategory command) {
        // 1. 상품 ID로 상품 조회
        if (!productRepository.existsById(command.productId())) {
            throw new NotFoundException(
                    "상품",
                    String.valueOf(command.productId())
            );
        }

        // 2. 카테고리 ID Set으로 카테고리 조회
        Set<Long> categoryIds = command.categoryIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int categoryCounts = categoryRepository.countByIdIn(categoryIds);
        if (!Objects.equals(categoryCounts, command.categoryIds().size())) {
            throw new NotFoundException(
                    "카테고리",
                    String.valueOf(command.categoryIds())
            );
        }

        // 3. 상품 카테고리 리스트 삭제
        return productCategoryRepository.deleteAllByProductIdAndCategoryIdIn(
                command.productId(),
                command.categoryIds()
        );
    }

    public void deleteAllByProductId(final long productId) {
        productCategoryRepository.deleteAllByProductId(productId);
    }

    public record CreateProductCategory(
            long productId,
            Set<Long> categoryIds
    ) {

    }

    public record DeleteProductCategory(
            long productId,
            Set<Long> categoryIds
    ) {

    }
}
