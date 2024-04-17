package org.example.bookweb.repository;

import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.BookSearchParameters;
import org.example.bookweb.models.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String TITLE_SPEC_KEY = "title";
    public static final String AUTHOR_SPEC_KEY = "author";
    public static final String ISBN_SPEC_KEY = "isbn";
    private final SpecificationProviderManager<Book> bookProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(bookProviderManager.getSpecificationProvider(TITLE_SPEC_KEY)
                    .getSpecification(searchParameters.titles()));
        }
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(bookProviderManager.getSpecificationProvider(AUTHOR_SPEC_KEY)
                    .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.isbns() != null && searchParameters.isbns().length > 0) {
            spec = spec.and(bookProviderManager.getSpecificationProvider(ISBN_SPEC_KEY)
                    .getSpecification(searchParameters.isbns()));
        }
        return spec;
    }
}
