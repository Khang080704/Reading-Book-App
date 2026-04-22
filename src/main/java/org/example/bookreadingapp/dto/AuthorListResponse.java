package org.example.bookreadingapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorListResponse {
    @JsonProperty("numFound")
    private int numFound;

    private List<OpenLibraryAuthorDTO> docs;
}
