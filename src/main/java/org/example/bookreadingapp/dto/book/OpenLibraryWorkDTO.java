package org.example.bookreadingapp.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
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
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.valueToTree(description);
                return jsonNode.get("value").asText();
            } catch (Exception e) {
                log.error("Error parsing description: {}", e.getMessage());
            }
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

