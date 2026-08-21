package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.reading.ChapterDto;
import org.example.bookreadingapp.entity.Chapter;
import org.example.bookreadingapp.repository.ChapterRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterService {
    private final ChapterRepository chapterRepository;

    public ChapterDto getChapterById(String chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow();

        return ChapterDto.builder()
                .id(chapter.getId())
                .content(chapter.getContent())
                .title(chapter.getTitle())
                .order(chapter.getIndexOrder())
                .build();
    }
}
