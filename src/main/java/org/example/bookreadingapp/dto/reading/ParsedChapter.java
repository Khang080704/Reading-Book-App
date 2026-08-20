package org.example.bookreadingapp.dto.reading;

public record ParsedChapter(
        int order,
        String title,
        String content
) {
}
