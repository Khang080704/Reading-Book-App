package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.Enum.ReadingMode;
import org.example.bookreadingapp.Enum.ResourceProvider;
import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.example.bookreadingapp.dto.reading.ParsedChapter;
import org.example.bookreadingapp.entity.Chapter;
import org.example.bookreadingapp.entity.ReadingResource;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.repository.ReadingResourceRepository;
import org.example.bookreadingapp.repository.WorkRepository;
import org.example.bookreadingapp.service.reader.epub.EpubDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookImportService {
    private final WorkRepository workRepository;
    private final ReadingResourceRepository readingResourceRepository;
    private final EpubDocumentReader epubDocumentReader;

    @Transactional
    public ReadingResource importEpub(String workKey, Resource epubResource, ResourceProvider resourceProvider) {
        Work work = workRepository.findByWorkKey(workKey)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Work not found: " + workKey
                        )
                );

        ParsedBook parsedBook = epubDocumentReader.read(epubResource);
        ReadingResource readingResource =
                ReadingResource.builder()
                        .work(work)
                        .resourceProvider(resourceProvider != null ? resourceProvider : ResourceProvider.INTERNAL)
                        .readingMode(ReadingMode.CHAPTER)
                        .build();


        for (ParsedChapter parsedChapter : parsedBook.chapters()) {

            Chapter chapter =
                    Chapter.builder()
                            .title(parsedChapter.title())
                            .indexOrder(parsedChapter.order())
                            .content(parsedChapter.content())
                            .build();

            readingResource.addChapter(chapter);
        }

        return readingResourceRepository.save(readingResource);

    }
}
