package org.example.bookreadingapp.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDetailDTO {
    private String workKey;
    private String title;
    private String description;
    private String coverUrl;
    private List<String> authorKeys;
}

