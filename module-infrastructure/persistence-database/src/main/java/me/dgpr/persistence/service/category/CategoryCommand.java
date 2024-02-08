package me.dgpr.persistence.service.category;

import me.dgpr.persistence.entity.category.CategoryEntity;
import me.dgpr.persistence.repository.category.CategoryRepository;
import me.dgpr.persistence.repository.store.StoreRepository;
import me.dgpr.persistence.service.store.exception.NotFoundStoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryCommand {

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public CategoryCommand(
            CategoryRepository categoryRepository,
            StoreRepository storeRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }

    public CategoryEntity createNewCategory(final CreateCategory command) {
        storeRepository.findById(command.storeId())
                .orElseThrow(() -> new NotFoundStoreException(String.valueOf(command.storeId())));

        CategoryEntity categoryEntity = CategoryEntity.create(
                command.storeId(),
                command.name()
        );

        return categoryRepository.save(categoryEntity);
    }


    public record CreateCategory(
            long storeId,
            String name
    ) {

    }
}
