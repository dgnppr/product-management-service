package me.dgpr.domains.category.usecase;

import me.dgpr.domains.category.domain.Category;

public interface CreateCategoryUseCase {

    Category command(Command command);

    record Command(
            long managerId,
            long storeId,
            String name
    ) {

    }

}
