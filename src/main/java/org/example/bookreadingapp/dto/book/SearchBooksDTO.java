package org.example.bookreadingapp.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchBooksDTO {
    @JsonProperty("numFound")
    private int numFound;

    @JsonProperty("docs")
    private List<BookSearchEntry> docs;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookSearchEntry {
        @JsonProperty("key")
        private String key;

        @JsonProperty("title")
        private String title;

        @JsonProperty("author_name")
        private List<String> authorNames;

        @JsonProperty("first_publish_year")
        private Integer firstPublishYear;

        @JsonProperty("isbn")
        private List<String> isbn;

        @JsonProperty("cover_i")
        private Long coverId;

        @JsonProperty("edition_count")
        private Integer editionCount;

        @JsonProperty("cover_edition_key")
        private String coverEditionKey;

        public String getCoverUrl() {
            return coverId != null ? "https://covers.openlibrary.org/a/olid/" + coverId + "-M.jpg" : null;
        }

        public String getFirstIsbn() {
            return isbn != null && !isbn.isEmpty() ? isbn.get(0) : null;
        }
    }
}
