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
public class OpenLibraryWorkDTO {
    @JsonProperty("key")
    private String key;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private Object description; // Can be string or object

    @JsonProperty("covers")
    private List<Long> covers;

    @JsonProperty("author_keys")
    private List<String> authorKeys;

    @JsonProperty("created")
    private Time created;

    @JsonProperty("last_modified")
    private Time modify;

    @JsonProperty("subjects")
    private List<String> subjects;

    @JsonProperty("subjects_people")
    private List<String> subjectPeople;

    @JsonProperty("subjects_places")
    private List<String> subjectPlaces;

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Time {
        @JsonProperty("type")
        private String type;

        @JsonProperty("value")
        private String value;
    }
}

