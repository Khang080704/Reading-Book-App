package org.example.bookreadingapp.dto.reading;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ChapterContentDto extends ChapterDto {
    private String prevChapterId;
    private String nextChapterId;
    private String content;
}
