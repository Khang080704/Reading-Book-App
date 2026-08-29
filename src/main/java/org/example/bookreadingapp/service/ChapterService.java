package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.reading.ChapterContentDto;
import org.example.bookreadingapp.dto.reading.ChapterDto;
import org.example.bookreadingapp.entity.Chapter;
import org.example.bookreadingapp.repository.ChapterRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterService {
    private final ChapterRepository chapterRepository;

    public ChapterDto getChapterById(String chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow();

        Optional<Chapter> prev = chapterRepository.findPreviousChapter(chapter.getIndexOrder(), chapter.getReadingResource().getId());
        Optional<Chapter> next = chapterRepository.findNextChapter(chapter.getIndexOrder(), chapter.getReadingResource().getId());

        return ChapterContentDto.builder()
                .content(chapter.getContent())
                .id(chapter.getId())
                .title(chapter.getTitle())
                .order(chapter.getIndexOrder())
                .nextChapterId(next.isPresent() ? next.get().getId() : null)
                .prevChapterId(prev.isPresent() ? prev.get().getId() : null)
                .resourceId(chapter.getReadingResource().getId())
                .build();
    }
}
