package org.example.bookweb.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Category response DTO")
@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}
