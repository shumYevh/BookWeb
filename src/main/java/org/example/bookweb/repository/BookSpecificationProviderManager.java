package org.example.bookweb.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.models.Book;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find specification provider for key: " + key)
        );
    }
}
