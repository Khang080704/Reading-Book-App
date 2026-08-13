package org.example.bookreadingapp.dto.reading;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookreadingapp.Enum.ReadingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingResourceDto {
    private boolean isAvailable;
    private String resourceId;
    private String provider;
    private ReadingMode readingMode;
}
