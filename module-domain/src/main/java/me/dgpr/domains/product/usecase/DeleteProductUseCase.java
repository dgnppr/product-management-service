package me.dgpr.domains.product.usecase;

public interface DeleteProductUseCase {

    void command(Command command);

    record Command(
            long productId,
            long managerId,
            long storeId
    ) {

    }
}
