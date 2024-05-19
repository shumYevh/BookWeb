package org.example.bookweb.repository.category;

import java.util.Optional;
import java.util.Set;
import org.example.bookweb.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends
        JpaRepository<Category,Long>,
        JpaSpecificationExecutor<Category> {
    Optional<Category> findCategoryById(Long id);

    Set<Category> findCategoriesByIdIn(Set<Long> ids);
}
