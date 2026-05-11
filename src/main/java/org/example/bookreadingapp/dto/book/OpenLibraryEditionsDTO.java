package org.example.bookreadingapp.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenLibraryEditionsDTO {
    @JsonProperty("entries")
    private List<EditionEntry> entries;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EditionEntry {
        @JsonProperty("key")
        private String key;

        @JsonProperty("title")
        private String title;

        @JsonProperty("isbn_13")
        private List<String> isbn13;

        @JsonProperty("isbn_10")
        private List<String> isbn10;

        @JsonProperty("number_of_pages")
        private Integer numberOfPages;

        @JsonProperty("publish_date")
        private String publishDate;

        @JsonProperty("publishers")
        private List<String> publishers;

        public String getIsbn() {
            if (isbn13 != null && !isbn13.isEmpty()) {
                return isbn13.get(0);
            } else if (isbn10 != null && !isbn10.isEmpty()) {
                return isbn10.get(0);
            }
            return null;
        }

        public String getPublisherName() {
            if (publishers != null && !publishers.isEmpty()) {
                return publishers.get(0);
            }
            return null;
        }
    }
}

