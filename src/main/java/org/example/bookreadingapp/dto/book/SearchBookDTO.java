package org.example.bookreadingapp.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBookDTO {
    private String bookKey;
    private String title;
    private String[] authorNames;
    private Integer firstPublishYear;
    private String isbn;
    private Integer editionCount;
    private String coverUrl;
}

