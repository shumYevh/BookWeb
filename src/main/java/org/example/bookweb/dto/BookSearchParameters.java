package org.example.bookweb.dto;

public record BookSearchParameters(String[] titles, String[] authors, String[] isbns) {

}
