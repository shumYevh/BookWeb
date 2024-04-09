package org.example.bookweb.dto;

import lombok.Data;

@Data
public class CreateBookRequestDto {
    private String title;
    private String author;
    private Double price;
    private String description;
    private String coverImage;
}
