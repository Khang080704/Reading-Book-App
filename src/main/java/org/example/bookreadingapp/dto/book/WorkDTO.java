package org.example.bookreadingapp.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkDTO implements Serializable {
    private String workKey;
    private String title;
    private String description;
    private String coverUrl;
}

