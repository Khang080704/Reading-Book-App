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

