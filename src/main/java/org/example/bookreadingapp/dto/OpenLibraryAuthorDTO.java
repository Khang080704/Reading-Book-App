package org.example.bookreadingapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryAuthorDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("birth_date")
    private String birthDay;

    @JsonProperty("key")
    private String key;

    @JsonProperty("readinglog_count")
    private int readingCount;
}
