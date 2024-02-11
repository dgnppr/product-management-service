package me.dgpr.persistence.repository.category;

import java.util.Set;
import me.dgpr.persistence.entity.category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT COUNT(ce) "
            + "FROM CategoryEntity ce "
            + "WHERE ce.id IN :categoryIds")
    int countByIdIn(@Param("categoryIds") Set<Long> categoryIds);

    int existsByIdIn(Set<Long> ids);
}
