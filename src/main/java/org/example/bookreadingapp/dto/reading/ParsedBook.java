package org.example.bookreadingapp.dto.reading;

import java.util.List;
import java.util.Map;

public record ParsedBook(
        String title,
        String author,
        String language,
        List<ParsedChapter> chapters
) {
}
