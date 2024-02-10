package me.dgpr.domains.product.service;

import java.util.Set;
import me.dgpr.domains.product.domain.Product;
import me.dgpr.domains.product.usecase.CreateProductUseCase;
import me.dgpr.persistence.common.Money;
import me.dgpr.persistence.entity.product.ProductEntity;
import me.dgpr.persistence.entity.product.ProductSize;
import me.dgpr.persistence.service.product.ProductCommand;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand.CreateProductCategory;
import me.dgpr.persistence.service.productname.ProductNameCommand;
import me.dgpr.persistence.service.productname.ProductNameCommand.CreateProductNames;
import org.springframework.stereotype.Service;

@Service
public class CreateProduct implements CreateProductUseCase {

    private final ProductService productService;
    private final ProductCommand productCommand;
    private final ProductCategoryCommand productCategoryCommand;
    private final ProductNameCommand productNameCommand;
    private final ProductNameSeparator productNameSeparator;

    public CreateProduct(
            ProductService productService,
            ProductCommand productCommand,
            ProductCategoryCommand productCategoryCommand,
            ProductNameCommand productNameCommand,
            ProductNameSeparator productNameSeparator
    ) {
        this.productService = productService;
        this.productCommand = productCommand;
        this.productCategoryCommand = productCategoryCommand;
        this.productNameCommand = productNameCommand;
        this.productNameSeparator = productNameSeparator;
    }

    @Override
    public Product command(final Command command) {
        // 1. 권한 여부 확인
        productService.verifyManagerPermission(
                command.managerId(),
                command.storeId()
        );

        // 2. product 저장
        ProductCommand.CreateProduct createProduct = createProductCommand(command);
        ProductEntity newProduct = productCommand.createNewProduct(createProduct);

        // 3. productCategories 저장
        CreateProductCategory createProductCategory = createProductCategoryCommand(
                newProduct,
                command.categoryIds()
        );
        productCategoryCommand.createProductCategory(createProductCategory);

        // 4. productNames 저장
        Set<String> words = productNameSeparator.separate(command.name());
        CreateProductNames createProductNames = createProductNamesCommand(
                newProduct,
                words
        );
        productNameCommand.createProductNames(createProductNames);

        return Product.from(newProduct);
    }

    private CreateProductNames createProductNamesCommand(ProductEntity newProduct,
            Set<String> words) {
        return new CreateProductNames(
                newProduct.getId(),
                words
        );
    }

    private CreateProductCategory createProductCategoryCommand(
            final ProductEntity newProduct,
            final Set<Long> categoryIds
    ) {
        return new CreateProductCategory(
                newProduct.getId(),
                categoryIds
        );
    }

    private ProductCommand.CreateProduct createProductCommand(final Command command) {
        return new ProductCommand.CreateProduct(
                command.storeId(),
                Money.of(command.price()),
                Money.of(command.cost()),
                command.name(),
                command.description(),
                command.barcode(),
                command.expirationDate(),
                ProductSize.from(command.size())
        );
    }
}
