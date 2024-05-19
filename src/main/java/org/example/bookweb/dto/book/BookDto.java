package org.example.bookweb.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import org.example.bookweb.models.Category;

@Schema(description = "Response DTO")
@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Double price;
    private String description;
    private String coverImage;
    private Set<Category> categories;
}
