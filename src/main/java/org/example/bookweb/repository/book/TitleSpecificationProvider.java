package org.example.bookweb.repository.book;

import java.util.Arrays;
import org.example.bookweb.models.Book;
import org.example.bookweb.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component

public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    public static final String KEY = "title";

    @Override
    public String getKey() {
        return KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get("title").in(Arrays.stream(params).toArray());
    }
}
