package org.example.bookreadingapp.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Author Works from Open Library API
 * https://openlibrary.org/dev/docs/api/authors (Works by an Author section)
 * Example: /authors/OL34221A/works.json
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorWorksDTO {
    @JsonProperty("entries")
    private List<WorkEntry> entries;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkEntry {
        @JsonProperty("key")
        private String key;

        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private Object description; // Can be string or object

        @JsonProperty("covers")
        private List<Long> covers;

        public String getDescription() {
            if (description instanceof String) {
                return (String) description;
            } else if (description != null) {
                return description.toString();
            }
            return null;
        }

        public String getCoverId() {
            if (covers != null && !covers.isEmpty()) {
                return String.valueOf(covers.get(0));
            }
            return null;
        }
    }
}

