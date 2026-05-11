package org.example.bookreadingapp.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BookDTO - General purpose DTO for Book entity
 * Can be used for caching and storing books
 * Currently SearchBookDTO is used for search results
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private String id;
    private String bookKey;
    private String title;
    private String isbn;
    private Integer editionCount;
    private Integer firstPublishYear;
    private String description;
}
