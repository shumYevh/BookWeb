package org.example.bookweb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.bookweb.validation.Isbn;

@Data
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @Isbn
    @NotNull
    private String isbn;
    @NotNull
    @Min(0)
    private Double price;
    private String description;
    @NotNull
    private String coverImage;
}
