package org.example.bookweb.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.bookweb.validation.Isbn;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @Isbn
    @NotBlank
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    @Size(min = 10, max = 200)
    private String description;
    @NotBlank
    private String coverImage;
    private Set<Long> categoryIds;
}
