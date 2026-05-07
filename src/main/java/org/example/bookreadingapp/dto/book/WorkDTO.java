package org.example.bookreadingapp.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkDTO {
    private String workKey;
    private String title;
    private String description;
    private String coverUrl;
}

