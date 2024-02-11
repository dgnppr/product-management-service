package me.dgpr.persistence.service.category;

import java.util.Set;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.entity.category.CategoryEntity;
import me.dgpr.persistence.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryQuery {

    private final CategoryRepository categoryRepository;

    public CategoryQuery(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity findById(final long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "카테고리",
                        String.valueOf(id))
                );
    }

    public void existsByIds(final Set<Long> ids) {
        if (categoryRepository.countByIdIn(ids) != ids.size()) {
            throw new NotFoundException(
                    "카테고리",
                    String.valueOf(ids)
            );
        }
    }
}
