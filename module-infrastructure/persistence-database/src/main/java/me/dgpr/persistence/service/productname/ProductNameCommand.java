package me.dgpr.persistence.service.productname;

import java.util.List;
import java.util.Set;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.entity.productname.ProductNameEntity;
import me.dgpr.persistence.repository.product.ProductRepository;
import me.dgpr.persistence.repository.productname.ProductNameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductNameCommand {

    private final ProductNameRepository productNameRepository;
    private final ProductRepository productRepository;

    public ProductNameCommand(
            ProductNameRepository productNameRepository,
            ProductRepository productRepository
    ) {
        this.productNameRepository = productNameRepository;
        this.productRepository = productRepository;
    }

    public int createProductNames(final CreateProductNames command) {
        // 1. 상품 ID로 상품 조회
        if (!productRepository.existsById(command.productId())) {
            throw new NotFoundException(
                    "상품",
                    String.valueOf(command.productId())
            );
        }

        // 2. 상품 이름 생성
        List<ProductNameEntity> productNames = command.names().stream()
                .map(name ->
                        ProductNameEntity.create(
                                command.productId(),
                                name
                        )
                ).toList();

        // 3. 상품 이름 저장
        return productNameRepository.saveAll(productNames).size();
    }

    public void deleteProductNamesByProductId(final DeleteProductNames command) {
        productNameRepository.deleteByProductIdAndNames(
                command.productId(),
                command.names()
        );
    }

    public void deleteAllByProductId(final long productId) {
        productNameRepository.deleteAllByProductId(productId);
    }

    public record CreateProductNames(
            long productId,
            Set<String> names) {

    }

    public record DeleteProductNames(
            long productId,
            Set<String> names

    ) {

    }
}
