package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.reading.ChapterDto;
import org.example.bookreadingapp.dto.reading.ReadingResourceDto;
import org.example.bookreadingapp.entity.Chapter;
import org.example.bookreadingapp.entity.ReadingResource;
import org.example.bookreadingapp.repository.ReadingResourceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadingResourceService {
    private final ReadingResourceRepository readingResourceRepository;

    public List<ReadingResourceDto> isReading(String workKey) {
        return readingResourceRepository.findByWorkKey(workKey).stream()
                .map(readingResource -> ReadingResourceDto.builder()
                        .isAvailable(true)
                        .resourceId(readingResource.getId())
                        .provider(readingResource.getResourceProvider().name())
                        .readingMode(readingResource.getReadingMode())
                        .language(readingResource.getLanguage())
                        .build())
                .toList();
    }

    public List<ChapterDto> getChaptersByReadingResourceId(String readingResourceId) {
        Set<Chapter> data = readingResourceRepository.getChaptersByReadingResourceId(readingResourceId).getChapters();
        List<ChapterDto> result = new ArrayList<>();

        for (Chapter chapter : data) {
            ChapterDto dto =
                    ChapterDto.builder()
                            .id(chapter.getId())
                            .order(chapter.getIndexOrder())
                            .title(chapter.getTitle())
                            .build();

            result.add(dto);
        }

        return result;
    }
}
