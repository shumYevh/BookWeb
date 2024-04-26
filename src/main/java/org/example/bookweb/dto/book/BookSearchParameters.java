package org.example.bookweb.dto.book;

public record BookSearchParameters(String[] titles, String[] authors, String[] isbns) {

}
