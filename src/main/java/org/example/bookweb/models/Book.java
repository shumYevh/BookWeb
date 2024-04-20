package org.example.bookweb.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    @Column(unique = true, nullable = false)
    private String isbn;
    private Double price;
    private String description;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = false;
}
