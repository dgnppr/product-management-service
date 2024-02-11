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

    public Page<ProductWithCategoriesDTO> findByStoreId(
            final long storeId,
            final Pageable pageable
    ) {
        Page<ProductEntity> products = productRepository.findAllByStoreId(
                storeId,
                pageable
        );

        // Product ID 목록 추출
        Set<Long> productIds = products.stream()
                .map(ProductEntity::getId)
                .collect(Collectors.toSet());

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

    public Page<ProductEntity> findAllByStoreId(
            final long storeId,
            final Pageable pageable
    ) {
        return productRepository.findAllByStoreId(
                storeId,
                pageable
        );
    }

    public Page<ProductEntity> findByName(
            final String name,
            final Pageable pageable
    ) {
        return productRepository.findByNameContaining(name, pageable);
    }
}
