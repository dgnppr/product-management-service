package me.dgpr.persistence.service.product;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.repository.category.dto.ProductCategoryDTO;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.product.dto.ProductWithCategoriesDTO;
import me.dgpr.persistence.repository.productcategory.ProductCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductQuery {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductQuery(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public ProductEntity findById(final long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "상품",
                        String.valueOf(id)
                ));
    }

    public ProductWithCategoriesDTO findByIdWithCategories(final long id) {

        // 상품 ID로 조회
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "상품",
                        String.valueOf(id)
                ));

        // 상품과 카테고리 목록을 조인하여 조회
        Map<Long, Set<String>> productIdToCategoryNames =
                productCategoryRepository.findByProductIdIn(Set.of(id))
                        .stream()
                        .collect(Collectors.groupingBy(
                                ProductCategoryDTO::productId,
                                Collectors.mapping(
                                        ProductCategoryDTO::categoryName,
                                        Collectors.toSet()
                                )
                        ));

        return new ProductWithCategoriesDTO(
                productEntity.getId(),
                productEntity.getStoreId(),
                productEntity.getPrice(),
                productEntity.getCost(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getBarcode(),
                productEntity.getExpirationDate(),
                productEntity.getSize(),
                productIdToCategoryNames.getOrDefault(productEntity.getId(), new HashSet<>())
        );
    }

    public Page<ProductWithCategoriesDTO> findAllByStoreId(
            final long storeId,
            final Pageable pageable
    ) {
        // 가게에 등록된 상품 조회
        Page<ProductEntity> products = productRepository.findAllByStoreId(
                storeId,
                pageable
        );

        // Product ID 목록 추출
        Set<Long> productIds = products.stream()
                .map(ProductEntity::getId)
                .collect(Collectors.toSet());

        // 상품 ID 목록으로 카테고리 목록 조회
        List<ProductCategoryDTO> productCategoryDTOs = productCategoryRepository.findByProductIdIn(
                productIds);

        Map<Long, Set<String>> productIdToCategoryNames = productCategoryDTOs.stream()
                .collect(Collectors.groupingBy(
                        ProductCategoryDTO::productId,
                        Collectors.mapping(
                                ProductCategoryDTO::categoryName,
                                Collectors.toSet()
                        )
                ));

        List<ProductWithCategoriesDTO> dtos = products.getContent().stream()
                .map(product -> new ProductWithCategoriesDTO(
                        product.getId(),
                        product.getStoreId(),
                        product.getPrice(),
                        product.getCost(),
                        product.getName(),
                        product.getDescription(),
                        product.getBarcode(),
                        product.getExpirationDate(),
                        product.getSize(),
                        productIdToCategoryNames.getOrDefault(product.getId(), new HashSet<>())
                ))
                .toList();

        return new PageImpl<>(dtos, pageable, products.getTotalElements());

    }

    public List<ProductEntity> findByStoreIdAndName(
            final long storeId,
            final String name
    ) {
        return productRepository.findByStoreIdAndName(
                storeId,
                name
        );
    }
}
