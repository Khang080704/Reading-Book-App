package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.Enum.BookFormat;
import org.example.bookreadingapp.Enum.ReadingMode;
import org.example.bookreadingapp.Enum.ResourceProvider;
import org.example.bookreadingapp.Enum.StorageType;
import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.example.bookreadingapp.dto.reading.ParsedChapter;
import org.example.bookreadingapp.entity.Chapter;
import org.example.bookreadingapp.entity.ReadingResource;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.repository.ReadingResourceRepository;
import org.example.bookreadingapp.repository.WorkRepository;
import org.example.bookreadingapp.service.filestorage.BookStorage;
import org.example.bookreadingapp.service.reader.BookDocumentReader;
import org.example.bookreadingapp.service.reader.epub.EpubDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookImportService {
    private final WorkRepository workRepository;
    private final ReadingResourceRepository readingResourceRepository;

    private final List<BookDocumentReader> readers;
    private final List<BookStorage> storages;

    @Transactional
    public ReadingResource importEpub(String workKey,
                                      BookFormat bookFormat,
                                      StorageType storageType,
                                      String storagePath,
                                      ResourceProvider resourceProvider) {
        Work work = workRepository.findByWorkKey(workKey)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Work not found: " + workKey
                        )
                );

        BookStorage storage = storages.stream()
                .filter(s -> s.support(storageType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No storage found for type: " + storageType
                        )
                );

        Resource epubResource = storage.load(storagePath);
        BookDocumentReader reader = readers.stream()
                .filter(r -> r.support(bookFormat))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No reader found for format: " + bookFormat
                        )
                );

        ParsedBook parsedBook = reader.read(epubResource);

        ReadingResource readingResource =
                ReadingResource.builder()
                        .work(work)
                        .resourceProvider(resourceProvider != null ? resourceProvider : ResourceProvider.INTERNAL)
                        .readingMode(ReadingMode.CHAPTER)
                        .language(parsedBook.language())
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
