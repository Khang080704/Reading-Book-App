package org.example.bookreadingapp.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditionDTO {
    private String editionKey;
    private String isbn;
    private Integer numberOfPages;
    private String publishDate;
    private String publisherName;
}

