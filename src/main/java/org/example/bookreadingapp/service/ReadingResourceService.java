package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.reading.ReadingResourceDto;
import org.example.bookreadingapp.repository.ReadingResourceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadingResourceService {
    private final ReadingResourceRepository readingResourceRepository;

    public ReadingResourceDto isReading(String workKey) {
        return readingResourceRepository.findByWorkKey(workKey)
                .map(readingResource -> ReadingResourceDto.builder()
                        .isAvailable(true)
                        .resourceId(readingResource.getId())
                        .provider(readingResource.getResourceProvider().name())
                        .readingMode(readingResource.getReadingMode())
                        .build())
                .orElseGet(() -> ReadingResourceDto.builder()
                        .isAvailable(false)
                        .build());
    }
}
