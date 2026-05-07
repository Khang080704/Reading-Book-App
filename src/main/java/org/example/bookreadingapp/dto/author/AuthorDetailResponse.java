package org.example.bookreadingapp.dto.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tools.jackson.databind.JsonNode;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorDetailResponse {
    @JsonProperty("birth_day")
    private String birthDate;

    @JsonProperty("name")
    private String fullName;

    private String bio;
    private String createdAt;
    private String lastModifiedAt;

    @JsonProperty("bio")
    public void unpackBio(JsonNode node) {
        this.bio = extractValue(node);
    }

    @JsonProperty("created")
    public void unpackCreated(JsonNode node) {
        this.createdAt = extractValue(node);
    }

    @JsonProperty("last_modified")
    public void unpackLastModified(JsonNode node) {
        this.lastModifiedAt = extractValue(node);
    }

    private String extractValue(JsonNode node) {
        if (node == null || node.isNull()) return null;
        if (node.isObject() && node.has("value")) {
            return node.get("value").asText();
        }
        return node.asText();
    }

}
