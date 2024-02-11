package me.dgpr.domains.product.service;

import me.dgpr.domains.product.usecase.DeleteProductUseCase;
import me.dgpr.persistence.service.product.ProductCommand;
import me.dgpr.persistence.service.productcategory.ProductCategoryCommand;
import me.dgpr.persistence.service.productname.ProductNameCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteProduct implements DeleteProductUseCase {

    private final ProductCommand productCommand;
    private final ProductCategoryCommand productCategoryCommand;
    private final ProductNameCommand productNameCommand;
    private final ProductService productService;

    public DeleteProduct(
            ProductCommand productCommand,
            ProductCategoryCommand productCategoryCommand,
            ProductNameCommand productNameCommand,
            ProductService productService
    ) {
        this.productCommand = productCommand;
        this.productCategoryCommand = productCategoryCommand;
        this.productNameCommand = productNameCommand;
        this.productService = productService;
    }

    @Override
    public void command(final Command command) {
        productService.verifyManagerPermission(
                command.managerId(),
                command.storeId()
        );

        productCategoryCommand.deleteAllByProductId(command.productId());

        productNameCommand.deleteAllByProductId(command.productId());

        productCommand.deleteById(command.productId());
    }
}
