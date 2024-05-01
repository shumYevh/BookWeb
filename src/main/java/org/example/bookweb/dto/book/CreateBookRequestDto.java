package org.example.bookweb.dto.book;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.bookweb.validation.Isbn;

@Data
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
    private Double price;
    @Size(min = 10, max = 200)
    private String description;
    @NotBlank
    private String coverImage;
}
