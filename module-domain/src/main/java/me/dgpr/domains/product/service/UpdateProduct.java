package me.dgpr.domains.product.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import me.dgpr.domains.product.domain.Product;
import me.dgpr.domains.product.usecase.UpdateProductUseCase;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.entity.productcategory.ProductCategoryEntity;
import me.dgpr.persistence.entity.productname.ProductNameEntity;
import me.dgpr.persistence.service.product.ProductCommand;
import me.dgpr.persistence.service.product.ProductQuery;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand.CreateProductCategory;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand.DeleteProductCategory;
import me.dgpr.persistence.service.productcategory.ProductCategoryQuery;
import me.dgpr.persistence.service.productname.ProductNameCommand;
import me.dgpr.persistence.service.productname.ProductNameQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateProduct implements UpdateProductUseCase {

    private final ProductService productService;
    private final ProductCommand productCommand;
    private final ProductQuery productQuery;
    private final ProductNameSeparator productNameSeparator;
    private final ProductNameCommand productNameCommand;
    private final ProductNameQuery productNameQuery;
    private final ProductCategoryQuery productCategoryQuery;
    private final ProductCategoryCommand productCategoryCommand;

    public UpdateProduct(
            ProductService productService,
            ProductQuery productQuery,
            ProductCommand productCommand,
            ProductNameSeparator productNameSeparator,
            ProductNameCommand productNameCommand,
            ProductNameQuery productNameQuery,
            ProductCategoryQuery productCategoryQuery,
            ProductCategoryCommand productCategoryCommand
    ) {
        this.productCommand = productCommand;
        this.productNameSeparator = productNameSeparator;
        this.productNameCommand = productNameCommand;
        this.productNameQuery = productNameQuery;
        this.productCategoryQuery = productCategoryQuery;
        this.productQuery = productQuery;
        this.productService = productService;
        this.productCategoryCommand = productCategoryCommand;
    }

    @Override
    public Product command(Command command) {
        productService.verifyManagerPermission(
                command.managerId(),
                command.storeId()
        );

        ProductEntity productEntity = productQuery.findById(command.productId());

        updateProductCategories(command);

        updateProductNames(command, productEntity);

        updateProduct(command);

        return Product.from(productEntity);
    }

    private void updateProduct(Command command) {
        ProductCommand.UpdateProduct updateProduct = new ProductCommand.UpdateProduct(
                Money.of(command.price()),
                Money.of(command.cost()),
                command.name(),
                command.description(),
                command.barcode(),
                command.expirationDate(),
                ProductSize.from(command.size())
        );

        productCommand.updateProduct(
                command.productId(),
                updateProduct
        );
    }

    private void updateProductNames(Command command, ProductEntity productEntity) {
        if (!productEntity.getName().equals(command.name())) {
            Set<String> existingNames = productNameQuery.findByProductId(command.productId())
                    .stream()
                    .map(ProductNameEntity::getName)
                    .collect(Collectors.toSet());

            Set<String> newNames = productNameSeparator.separate(command.name());

            Set<String> namesToAdd = new HashSet<>(newNames);
            namesToAdd.removeAll(existingNames);

            Set<String> namesToRemove = new HashSet<>(existingNames);
            namesToRemove.removeAll(newNames);

            productNameCommand.createProductNames(
                    new ProductNameCommand.CreateProductNames(
                            command.productId(),
                            namesToAdd
                    )
            );

            productNameCommand.deleteProductNamesByProductId(
                    new ProductNameCommand.DeleteProductNames(
                            command.productId(),
                            namesToRemove
                    )
            );
        }
    }

    private void updateProductCategories(Command command) {
        Set<Long> currentCategoryIds = productCategoryQuery.findByProductId(command.productId())
                .stream()
                .map(ProductCategoryEntity::getCategoryId)
                .collect(Collectors.toSet());

        Set<Long> newCategoryIds = new HashSet<>(command.categoryIds());

        if (!currentCategoryIds.equals(newCategoryIds)) {

            Set<Long> categoriesToRemove = new HashSet<>(currentCategoryIds);
            categoriesToRemove.removeAll(newCategoryIds);

            Set<Long> categoriesToAdd = new HashSet<>(newCategoryIds);
            categoriesToAdd.removeAll(currentCategoryIds);

            productCategoryCommand.deleteProductCategory(
                    new DeleteProductCategory(
                            command.productId(),
                            categoriesToRemove
                    )
            );

            productCategoryCommand.createProductCategory(
                    new CreateProductCategory(
                            command.productId(),
                            categoriesToAdd
                    )
            );
        }
    }
}
